package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.AnneeAcademique;
import com.api.gestnotesapi.repository.AnneeAcademiqueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnneeAcademiqueService {

    private AnneeAcademiqueRepo anneeAcademiqueRepo;

    @Autowired
    public AnneeAcademiqueService(AnneeAcademiqueRepo anneeAcademiqueRepo) {
        this.anneeAcademiqueRepo = anneeAcademiqueRepo;
    }

    public List<AnneeAcademique> getAll(){
        return anneeAcademiqueRepo.findAll();
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

    public void delete(Long id) {
        anneeAcademiqueRepo.deleteById(id);
    }
}
