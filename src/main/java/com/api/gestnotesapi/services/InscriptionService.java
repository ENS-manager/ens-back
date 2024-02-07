package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.AnneeAcademique;
import com.api.gestnotesapi.entities.Etudiant;
import com.api.gestnotesapi.entities.Inscription;
import com.api.gestnotesapi.entities.Parcours;
import com.api.gestnotesapi.repository.AnneeAcademiqueRepo;
import com.api.gestnotesapi.repository.EtudiantRepo;
import com.api.gestnotesapi.repository.InscriptionRepo;
import com.api.gestnotesapi.repository.ParcoursRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InscriptionService {

    private InscriptionRepo inscriptionRepo;
    private EtudiantRepo etudiantRepo;
    private AnneeAcademiqueRepo anneeAcademiqueRepo;
    private ParcoursRepo parcoursRepo;


    @Autowired
    public InscriptionService(InscriptionRepo inscriptionRepo, ParcoursRepo parcoursRepo, EtudiantRepo etudiantRepo, AnneeAcademiqueRepo anneeAcademiqueRepo) {
        this.inscriptionRepo = inscriptionRepo;
        this.parcoursRepo = parcoursRepo;
        this.etudiantRepo = etudiantRepo;
        this.anneeAcademiqueRepo = anneeAcademiqueRepo;
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
