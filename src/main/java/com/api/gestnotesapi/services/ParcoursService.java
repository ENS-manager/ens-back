package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParcoursService {

    private OptionRepo optionRepo;
    private NiveauRepo niveauRepo;
    private ParcoursRepo parcoursRepo;
    private EtudiantRepo etudiantRepo;
    private InscriptionRepo inscriptionRepo;
    private AnneeAcademiqueRepo anneeAcademiqueRepo;

    @Autowired
    public ParcoursService(OptionRepo optionRepo, NiveauRepo niveauRepo, ParcoursRepo parcoursRepo, EtudiantRepo etudiantRepo, InscriptionRepo inscriptionRepo, AnneeAcademiqueRepo anneeAcademiqueRepo) {
        this.optionRepo = optionRepo;
        this.niveauRepo = niveauRepo;
        this.parcoursRepo = parcoursRepo;
        this.etudiantRepo = etudiantRepo;
        this.inscriptionRepo = inscriptionRepo;
        this.anneeAcademiqueRepo = anneeAcademiqueRepo;
    }

    public Parcours getParcoursByOptionAndNiveau(int level, String code){

        Option option = optionRepo.findByCode(code).orElse(null);
        Niveau niveau = niveauRepo.findByValeur(level).orElse(null);

        if (option == null || niveau == null) {
            return null;
        }

        return parcoursRepo.findByOptionAndNiveau(option, niveau);
    }

    public Parcours getParcoursEtudiant(Long id, String anneeAca){
        Etudiant etudiant = etudiantRepo.findById(id).orElse(null);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByCode(anneeAca).orElse(null);
        if (etudiant == null || anneeAcademique == null){
            return null;
        }
        return inscriptionRepo.findByEtudiantAndAnneeAcademique(etudiant, anneeAcademique).getParcours();
    }
}
