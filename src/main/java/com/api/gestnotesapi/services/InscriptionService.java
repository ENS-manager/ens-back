package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InscriptionService {

    private InscriptionRepo inscriptionRepo;
    private EtudiantRepo etudiantRepo;
    private AnneeAcademiqueRepo anneeAcademiqueRepo;
    private ParcoursRepo parcoursRepo;
    private OptionRepo optionRepo;
    private NiveauRepo niveauRepo;

    @Autowired
    public InscriptionService(InscriptionRepo inscriptionRepo, ParcoursRepo parcoursRepo, EtudiantRepo etudiantRepo, AnneeAcademiqueRepo anneeAcademiqueRepo, OptionRepo optionRepo, NiveauRepo niveauRepo) {
        this.inscriptionRepo = inscriptionRepo;
        this.parcoursRepo = parcoursRepo;
        this.etudiantRepo = etudiantRepo;
        this.anneeAcademiqueRepo = anneeAcademiqueRepo;
        this.optionRepo = optionRepo;
        this.niveauRepo = niveauRepo;
    }


    public Inscription addInscription(Inscription inscription) {
        if (inscription == null){
            return null;
        }
        return inscriptionRepo.save(inscription);
    }

    public Inscription getById(Long id) {
        Inscription inscription = inscriptionRepo.findById(id).orElse(null);
        if (inscription == null){
            return null;
        }
        return inscription;
    }

    public Inscription update(Long id, Inscription inscription) {
        Inscription inscriptionFromDb = inscriptionRepo.findById(id).orElse(null);
        if (inscriptionFromDb == null){
            return null;
        }
        inscriptionFromDb.setEtudiant(inscription.getEtudiant());
        inscriptionFromDb.setAnneeAcademique(inscription.getAnneeAcademique());
        inscriptionFromDb.setParcours(inscription.getParcours());

        return inscriptionRepo.save(inscriptionFromDb);
    }

    public String delete(Long id) {
        Inscription inscription = getById(id);
        if (inscription == null){
            return "Aucun objet trouve pour l'id specifie";
        }
        inscription.setActive(false);
        inscriptionRepo.save(inscription);

        return "Operation reussi avec succes";
    }

    public Inscription getByEtudiantAndAnneeAcademique(Etudiant etudiant, AnneeAcademique anneeAcademique) {
        Inscription inscription = inscriptionRepo.findByEtudiantAndAnneeAcademique(etudiant, anneeAcademique);
        if (inscription == null){
            return null;
        }
        return inscription;
    }

}
