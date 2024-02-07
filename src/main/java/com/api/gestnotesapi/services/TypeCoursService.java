package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.AnneeAcademique;
import com.api.gestnotesapi.entities.TypeCours;
import com.api.gestnotesapi.repository.TypeCoursRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeCoursService {

    private TypeCoursRepo typeCoursRepo;

    @Autowired
    public TypeCoursService(TypeCoursRepo typeCoursRepo) {
        this.typeCoursRepo = typeCoursRepo;
    }

    public TypeCours getById(Long id) {
        TypeCours typeCours = typeCoursRepo.findById(id).orElse(null);
        if (typeCours == null){
            return null;
        }
        return typeCours;
    }

    public TypeCours addTypeCours(TypeCours typeCours) {
        if (typeCours == null){
            return null;
        }
        return typeCoursRepo.save(typeCours);
    }

    public List<TypeCours> getAll() {
        List<TypeCours> typeCoursList = typeCoursRepo.findAll();
        if (typeCoursList == null){
            return null;
        }
        return typeCoursList;
    }

    public TypeCours update(Long id, TypeCours typeCours) {
        TypeCours update = getById(id);
        if (update == null){
            return null;
        }
        update.setNom(typeCours.getNom());
        return typeCoursRepo.save(update);
    }

    public String delete(Long id) {
        TypeCours typeCours = getById(id);
        if (typeCours == null){
            return "Aucun objet trouve pour l'id specifie";
        }
        typeCours.setActive(false);
        typeCoursRepo.save(typeCours);

        return "Operation reussi avec succes";
    }
}
