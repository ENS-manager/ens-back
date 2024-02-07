package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.AnneeAcademique;
import com.api.gestnotesapi.entities.Departement;
import com.api.gestnotesapi.repository.DepartementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartementService {

    private DepartementRepo departementRepo;

    @Autowired
    public DepartementService(DepartementRepo departementRepo) {
        this.departementRepo = departementRepo;
    }

    public Departement addDepartement(Departement departement) {
        if (departement == null){
            return null;
        }
        return departementRepo.save(departement);
    }

    public List<Departement> getAll() {
        List<Departement> list = departementRepo.findAll();
        if (list == null){
            return null;
        }
        return list;
    }

    public Departement getByCode(String code) {
        Departement departement = departementRepo.findByCode(code).orElse(null);
        if (departement == null){
            return null;
        }
        return departement;
    }

    public Departement getById(Long id) {
        Departement departement = departementRepo.findById(id).orElse(null);
        if (departement == null){
            return null;
        }
        return departement;
    }

    public Departement updateById(Long id, Departement departement) {
        Departement upate = getById(id);
        if (upate == null){
            return null;
        }
        return upate;
    }

    public String delete(Long id) {
        Departement departement = getById(id);
        if (departement == null){
            return "Aucun objet trouve pour l'id specifie";
        }
        departement.setActive(false);
        departementRepo.save(departement);

        return "Operation reussi avec succes";
    }
}
