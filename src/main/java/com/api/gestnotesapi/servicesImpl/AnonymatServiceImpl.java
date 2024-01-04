package com.api.gestnotesapi.servicesImpl;

import com.api.gestnotesapi.services.AnonymatService;

import java.time.LocalDate;
import java.util.List;

public class AnonymatServiceImpl implements AnonymatService {
    @Override
    public String anonymatGenerator(String codeUE, int session, int n) {
        int annee = LocalDate.now().getYear();
        int year = annee-2000;
        return String.valueOf(codeUE + session + year + "_" + n);
    }
}
