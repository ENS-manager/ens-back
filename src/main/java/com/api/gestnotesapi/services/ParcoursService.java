package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParcoursService {

    private ParcoursRepo parcoursRepo;
    private NiveauService niveauServicee;
    private OptionService optionService;
    private EtudiantService etudiantService;
    private AnneeAcademiqueService anneeAcademiqueService;
    private InscriptionService inscriptionService;
    private DepartementService departementService;


    @Autowired
    public ParcoursService(ParcoursRepo parcoursRepo, NiveauService niveauServicee, ParcoursService parcoursService, DepartementService departementService, OptionService optionService, EtudiantService etudiantService, AnneeAcademiqueService anneeAcademiqueService, InscriptionService inscriptionService) {
        this.optionService = optionService;
        this.etudiantService = etudiantService;
        this.anneeAcademiqueService = anneeAcademiqueService;
        this.inscriptionService = inscriptionService;
        this.niveauServicee = niveauServicee;
        this.parcoursRepo = parcoursRepo;
        this.departementService = departementService;
    }

    public Parcours getParcoursByOptionAndNiveau(int level, String code){

        Option option = optionService.getByCode(code);
        Niveau niveau = niveauServicee.getByValeur(level);

        if (option == null || niveau == null) {
            return null;
        }

        return parcoursRepo.findByOptionAndNiveau(option, niveau);
    }

    public Parcours getParcoursEtudiant(Long id, String anneeAca){
        Etudiant etudiant = etudiantService.getById(id);
        AnneeAcademique anneeAcademique = anneeAcademiqueService.getByCode(anneeAca);
        if (etudiant == null || anneeAcademique == null){
            return null;
        }
        return inscriptionService.getByEtudiantAndAnneeAcademique(etudiant, anneeAcademique).getParcours();
    }

    public Parcours getByLabel(String label) {
        Parcours parcours = parcoursRepo.findByLabel(label).orElse(null);
        if (parcours == null){
            return null;
        }
        return parcours;
    }

    public Parcours addParcours(Parcours parcours) {
        if (parcours == null){
            return null;
        }
        if (parcours.getLabel() != null){
            return parcoursRepo.save(parcours);
        }
        Niveau niveau = niveauServicee.getById(parcours.getNiveau().getId());
        Option option = optionService.getById( parcours.getOption().getId());
        String code = option.getCode();
        int valeur = niveau.getValeur();
        String label = code+" "+valeur;
        parcours.setLabel(label);
        return parcoursRepo.save(parcours);
    }

    public List<Parcours> getAll() {
        List<Parcours> list = parcoursRepo.findAll();
        if (list == null){
            return null;
        }
        return list;
    }

    public Parcours getById(Long id) {
        Parcours parcours = parcoursRepo.findById(id).orElse(null);
        if (parcours == null){
            return null;
        }
        return parcours;
    }

    public List<Parcours> getAllByDepartement(String code) {
        Departement departement = departementService.getByCode(code);
        if (departement == null){
            return null;
        }
        List<Parcours> parcoursList = new ArrayList<>();
        for (Parcours parcours : parcoursRepo.findAll()){
            Option option = optionService.getById(parcours.getOption().getId());
            Departement depart = departementService.getById(option.getDepartement().getId());
            if (depart.getCode().equals(departement.getCode())){
                parcoursList.add(parcours);
            }
        }
        if (parcoursList == null){
            return null;
        }
        return parcoursList;
    }

    public void delete(Long id) {
        parcoursRepo.deleteById(id);
    }
}
