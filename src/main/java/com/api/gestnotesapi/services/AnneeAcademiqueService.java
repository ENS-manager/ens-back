package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.AnneeAcademique;
import com.api.gestnotesapi.repository.AnneeAcademiqueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnneeAcademiqueService {

    private AnneeAcademiqueRepo anneeAcademiqueRepo;

    @Autowired
    public AnneeAcademiqueService(AnneeAcademiqueRepo anneeAcademiqueRepo) {
        this.anneeAcademiqueRepo = anneeAcademiqueRepo;
    }

    public List<AnneeAcademique> getAll(){
        List<AnneeAcademique> anneeAcademiques = anneeAcademiqueRepo.findAll();
        if (anneeAcademiques == null){
            return null;
        }
        return anneeAcademiques;
    }

    public AnneeAcademique addAnneeAcademique(AnneeAcademique anneeAcademique){

        if (anneeAcademique == null){
            return null;
        }
        int debut = anneeAcademique.getDebut().getYear();
        int fin = anneeAcademique.getFin().getYear();
        String code = debut + "-" + fin;
        anneeAcademique.setNumeroDebut(debut);
        anneeAcademique.setCode(code);
        return anneeAcademiqueRepo.save(anneeAcademique);

    }

    public AnneeAcademique getById(Long id){
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findById(id).orElse(null);
        if (anneeAcademique == null){
            return null;
        }
        return anneeAcademique;
    }

    public AnneeAcademique updateById(Long id, AnneeAcademique anneeAcademique){

        AnneeAcademique anneeAcaFromDb = anneeAcademiqueRepo.findById(id).orElse(null);
        if (anneeAcaFromDb == null){
            return null;
        }
        anneeAcaFromDb.setDebut(anneeAcademique.getDebut());
        anneeAcaFromDb.setNumeroDebut(anneeAcademique.getDebut().getYear());
        anneeAcaFromDb.setFin(anneeAcademique.getFin());
        anneeAcaFromDb.setCode(anneeAcademique.getCode());

        return anneeAcademiqueRepo.save(anneeAcaFromDb);
    }

    public String delete(Long id) {
        AnneeAcademique anneeAcademique = getById(id);
        if (anneeAcademique == null){
            return "Aucun objet trouve pour l'id specifie";
        }
        anneeAcademique.setActive(false);
        anneeAcademiqueRepo.save(anneeAcademique);

        return "Operation reussi avec succes";
    }

    public AnneeAcademique getByCode(String anneeAca) {
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByCode(anneeAca).orElse(null);
        if (anneeAcademique == null){
            return null;
        }
        return anneeAcademique;
    }

    public AnneeAcademique getByYear(int year) {
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        if (anneeAcademique == null){
            return null;
        }
        return anneeAcademique;
    }

    public List<AnneeAcademique> getAllActif() {
        List<AnneeAcademique> anneeAcademiqueList = getAll();
        List<AnneeAcademique> anneeAcademiques = new ArrayList<>();
        for (AnneeAcademique anneeAcademique : anneeAcademiqueList){
            if (anneeAcademique.getActive().equals(true)){
                anneeAcademiques.add(anneeAcademique);
            }
        }
        return anneeAcademiques;
    }
}
