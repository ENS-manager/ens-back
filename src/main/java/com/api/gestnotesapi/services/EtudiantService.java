package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class EtudiantService {

    private EtudiantRepo etudiantRepo;
    private InscriptionRepo inscriptionRepo;
    private ParcoursService parcoursService;
    private AnneeAcademiqueRepo anneeAcademiqueRepo;
    private DepartementRepo departementRepo;
    private OptionRepo optionRepo;

    @Autowired
    public EtudiantService(EtudiantRepo etudiantRepo, InscriptionRepo inscriptionRepo, ParcoursService parcoursService, AnneeAcademiqueRepo anneeAcademiqueRepo, DepartementRepo departementRepo, OptionRepo optionRepo) {
        this.etudiantRepo = etudiantRepo;
        this.inscriptionRepo = inscriptionRepo;
        this.parcoursService = parcoursService;
        this.anneeAcademiqueRepo = anneeAcademiqueRepo;
        this.departementRepo = departementRepo;
        this.optionRepo = optionRepo;
    }

    public List<Etudiant> getListEtudiantByParcours(String label, int year){

        List<Etudiant> etudiantList = new ArrayList<>();
        Parcours parcours = parcoursService.getByLabel(label);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);

        if (parcours == null || anneeAcademique == null){
            return null;
        }
        List<Inscription> inscriptions = inscriptionRepo.findAllByParcoursAndAnneeAcademiqueAndActive(parcours, anneeAcademique, true);
        for (Inscription inscription : inscriptions){
            if (inscription.getEtudiant().getActive().equals(true)){
                etudiantList.add(inscription.getEtudiant());
            }
        }

        if (etudiantList.isEmpty()){
            return null;
        }
        return etudiantList;
    }

    public List<Etudiant> getEtudiantByDepartement(String code){
        List<Etudiant> etudiantList = new ArrayList<>();
        Departement departement = departementRepo.findByCode(code).orElse(null);
        if (departement == null){
            return null;
        }
        for (Inscription inscription: inscriptionRepo.findAll()){
            Parcours parcours = parcoursService.getById(inscription.getParcours().getId());
            Option option = optionRepo.findById(parcours.getOption().getId()).get();
            Departement depart = departementRepo.findById(option.getDepartement().getId()).get();

            if (depart.getCode().equals(departement.getCode())){
                Etudiant etudiant = etudiantRepo.findById(inscription.getEtudiant().getId()).get();
                etudiantList.add(etudiant);
            }
        }
        return etudiantList;
    }

    public Etudiant addEtudiant(Etudiant etudiant) {
        if (etudiant == null){
            return null;
        }
        Etudiant updateEtudiant = etudiantRepo.save(etudiant);
        if (updateEtudiant.getMatricule() != null){
            return updateEtudiant;
        }
        MatriculeService matriculeService = new MatriculeService();
        String matricule = matriculeService.matriculeGenerator(updateEtudiant.getId());
        updateEtudiant.setMatricule(matricule);
        return etudiantRepo.save(updateEtudiant);
    }

    public List<Etudiant> getAll() {
        List<Etudiant> list = etudiantRepo.findAll();
        if (list == null){
            return null;
        }
        return list;
    }

    public Etudiant getById(Long id) {
        Etudiant etudiant = etudiantRepo.findById(id).orElse(null);
        if (etudiant == null){
            return null;
        }
        return etudiant;
    }

    public Etudiant getByMatricule(String matricule) {
        Etudiant etudiant = etudiantRepo.findByMatricule(matricule).orElse(null);
        if (etudiant == null){
            return null;
        }
        return etudiant;
    }

    public Etudiant updateById(Long id, Etudiant etudiant) {
        Etudiant update = etudiantRepo.findById(id).orElse(null);
        if (update == null){
            return null;
        }
        update.setEmail(etudiant.getEmail());
        update.setGenre(etudiant.getGenre());
        update.setMatricule(etudiant.getMatricule());
        update.setNom(etudiant.getNom());
        update.setDateDeNaissance(etudiant.getDateDeNaissance());
        update.setLieuDeNaissance(etudiant.getLieuDeNaissance());
        update.setNumeroTelephone(etudiant.getNumeroTelephone());
        update.setRegion(etudiant.getRegion());
        update.setType(etudiant.getType());

        return etudiantRepo.save(update);
    }

    public String delete(Long id) {
        Etudiant etudiant = getById(id);
        if (etudiant == null){
            return "Aucun objet trouve pour l'id specifie";
        }
        etudiant.setActive(false);
        etudiantRepo.save(etudiant);

        return "Operation reussi avec succes";
    }

    public List<Etudiant> getEtudiantByDepartementAndAnnee(String code, int year) {
        Departement departement = departementRepo.findByCode(code).orElse(null);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        if (departement == null || anneeAcademique == null){
            return null;
        }

        List<Parcours> parcoursList = parcoursService.getAllByDepartement(departement.getCode());
        List<Etudiant> etudiantList = new ArrayList<>();
        for (Parcours parcours : parcoursList){
            if (getListEtudiantByParcours(parcours.getLabel(), anneeAcademique.getNumeroDebut()) != null){
                etudiantList.addAll(getListEtudiantByParcours(parcours.getLabel(), anneeAcademique.getNumeroDebut()));
            }
        }

        return etudiantList;
    }

    public List<Etudiant> getAllByAnnee(int year) {
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        List<Inscription> inscriptions = inscriptionRepo.findAllByAnneeAcademique(anneeAcademique);
        if (inscriptions == null){
            return null;
        }
        List<Etudiant> etudiantList = new ArrayList<>();
        for (Inscription inscription : inscriptions){
            etudiantList.add(inscription.getEtudiant());
        }
        return etudiantList;
    }

    public List<Etudiant> getListEtudiantByParcoursAndActive(String label, Integer numeroDebut) {
        List<Etudiant> etudiantList = new ArrayList<>();
        Parcours parcours = parcoursService.getByLabel(label);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(numeroDebut);

        if (parcours == null || anneeAcademique == null){
            return null;
        }
        List<Inscription> inscriptions = inscriptionRepo.findAllByParcoursAndAnneeAcademiqueAndActive(parcours, anneeAcademique, true);
        for (Inscription inscription : inscriptions){
            if (inscription.getEtudiant().getActive().equals(true)){
                etudiantList.add(inscription.getEtudiant());
            }
        }

        if (etudiantList.isEmpty()){
            return null;
        }
        return etudiantList;
    }

//    pour le pv grand Jury
//    public List<Etudiant> getListEtudiantByOption(String code){
//        Option option = optionRepo.findByCode(code).orElse(null);
//        if (option == null){
//            return null;
//        }
//        List<Parcours> parcoursList = parcoursService.getListParcoursByOption(option.getCode());
//        List<Etudiant> etudiantList = new ArrayList<>();
//        for (Parcours parcours : parcoursList){
//            etudiantList.addAll(getListEtudiantByParcoursSimple(parcours.getLabel()));
//        }
//        return etudiantList;
//    }


}
