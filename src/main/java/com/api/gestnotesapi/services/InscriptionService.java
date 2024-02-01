package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.Parcours;
import com.api.gestnotesapi.repository.EtudiantRepo;
import com.api.gestnotesapi.repository.InscriptionRepo;
import com.api.gestnotesapi.repository.ParcoursRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InscriptionService {

    private InscriptionRepo inscriptionRepo;
    private ParcoursRepo parcoursRepo;
    private EtudiantRepo etudiantRepo;


    @Autowired
    public InscriptionService(InscriptionRepo inscriptionRepo, ParcoursRepo parcoursRepo, EtudiantRepo etudiantRepo) {
        this.inscriptionRepo = inscriptionRepo;
        this.parcoursRepo = parcoursRepo;
        this.etudiantRepo = etudiantRepo;
    }


}
