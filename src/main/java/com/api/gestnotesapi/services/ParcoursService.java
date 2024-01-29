package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.Niveau;
import com.api.gestnotesapi.entities.Option;
import com.api.gestnotesapi.entities.Parcours;
import com.api.gestnotesapi.repository.NiveauRepo;
import com.api.gestnotesapi.repository.OptionRepo;
import com.api.gestnotesapi.repository.ParcoursRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ParcoursService {

    private OptionRepo optionRepo;
    private NiveauRepo niveauRepo;
    private ParcoursRepo parcoursRepo;

    @Autowired
    public ParcoursService(OptionRepo optionRepo, NiveauRepo niveauRepo, ParcoursRepo parcoursRepo) {
        this.optionRepo = optionRepo;
        this.niveauRepo = niveauRepo;
        this.parcoursRepo = parcoursRepo;
    }

    public Parcours getParcoursByOptionAndNiveau(int level, String code){

        Option option = optionRepo.findByCode(code).orElse(null);
        Niveau niveau = niveauRepo.findByValeur(level).orElse(null);

        if (option == null || niveau == null) {
            return null;
        }

        return parcoursRepo.findByOptionAndNiveau(option, niveau);
    }
}
