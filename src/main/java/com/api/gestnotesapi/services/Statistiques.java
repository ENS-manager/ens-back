package com.api.gestnotesapi.services;

import com.api.gestnotesapi.dto.StatPassedDepart;
import com.api.gestnotesapi.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class Statistiques {

    private AnneeAcademiqueService anneeAcademiqueService;
    private EtudiantService etudiantService;
    private CoursService coursService;
    private ParcoursService parcoursService;
    private NoteService noteService;
    private DepartementService departementService;

    @Autowired
    public Statistiques(AnneeAcademiqueService anneeAcademiqueService, EtudiantService etudiantService, CoursService coursService, ParcoursService parcoursService, NoteService noteService, DepartementService departementService) {
        this.anneeAcademiqueService = anneeAcademiqueService;
        this.etudiantService = etudiantService;
        this.coursService = coursService;
        this.parcoursService = parcoursService;
        this.noteService = noteService;
        this.departementService = departementService;
    }

    public Integer statAnneeActif(){
        List<AnneeAcademique> anneeAcademiqueList = new ArrayList<>();
        for (AnneeAcademique anneeAcademique : anneeAcademiqueService.getAll()){
            if (anneeAcademique.getActive().equals(true)){
                anneeAcademiqueList.add(anneeAcademique);
            }
        }
        return anneeAcademiqueList.size();
    }

    public Integer statDepartActif(){
        List<Departement> departementList = new ArrayList<>();
        for (Departement departement : departementService.getAll()){
            if (departement.getActive().equals(true)){
                departementList.add(departement);
            }
        }
        return departementList.size();
    }

    public Integer statAllEtudiantActif(){
        List<Etudiant> etudiantList = new ArrayList<>();
        int year = anneeAca();
        List<Etudiant> etudiants = etudiantService.getAllByAnnee(year);
        if (etudiants == null){
            return 0;
        }
        for (Etudiant etudiant : etudiants){
            if (etudiant.getActive().equals(true)){
                etudiantList.add(etudiant);
            }
        }
        return etudiantList.size();
    }

    public Integer anneeAca(){
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueService.getAllActif();
        int year = 0;
        for (AnneeAcademique anneeAcademique : anneeAcademiqueList){
            if (anneeAcademique.getNumeroDebut() > year){
                year = anneeAcademique.getNumeroDebut();
            }
        }
        return year;
    }

    public Integer statEtudiantDepartementActif(String code){
        Departement departement = departementService.getByCodeAndActive(code, true);
        if (departement == null){
            return 0;
        }
        int year = anneeAca();
        List<Etudiant> etudiantList = new ArrayList<>();
        List<Etudiant> etudiants = etudiantService.getEtudiantByDepartementAndAnnee(departement.getCode(), year);
        if (etudiants == null){
            return 0;
        }
        for (Etudiant etudiant : etudiants){
            if (etudiant.getActive().equals(true)){
                etudiantList.add(etudiant);
            }
        }
        return etudiantList.size();
    }

    public Integer statEtudiantParcoursActif(String label){
        Parcours parcours = parcoursService.getByLabelAndActive(label);
        if (parcours == null){
            return 0;
        }
        int year = anneeAca();
        List<Etudiant> etudiantList = new ArrayList<>();
        List<Etudiant> etudiants =  etudiantService.getListEtudiantByParcours(parcours.getLabel(), year);
        if (etudiants == null){
            return 0;
        }
        for (Etudiant etudiant :etudiants){
            if (etudiant.getActive().equals(true)){
                etudiantList.add(etudiant);
            }
        }
        return etudiantList.size();
    }

    public Integer statAllCoursActif(){
        List<Cours> coursList = coursService.getAll();
        if (coursList == null){
            return 0;
        }
        List<Cours> cours = new ArrayList<>();
        for (Cours cour : coursList){
            if (cour.getActive().equals(true)){
                cours.add(cour);
            }
        }
        return cours.size();
    }

    public Integer statAllCoursDepartementActif(String code){
        List<Cours> coursList = new ArrayList<>();
        List<Cours> cours = coursService.getAllCoursByDepartement(code);
        if (cours == null){
            return 0;
        }
        for (Cours cour : cours){
            if (cour.getActive().equals(true)){
                coursList.add(cour);
            }
        }
        return coursList.size();
    }

    public Integer statAllParcoursActif(){
        List<Parcours> parcours = parcoursService.getAll();
        if (parcours == null){
            return 0;
        }
        List<Parcours> parcoursList = new ArrayList<>();
        for (Parcours parcour : parcours){
            if (parcour.getActive().equals(true)){
                parcoursList.add(parcour);
            }
        }
        return parcoursList.size();
    }

    public Integer statAllParcoursDepartementActif(String code){
        Departement departement = departementService.getByCode(code);
        if (departement == null){
            return 0;
        }
        List<Parcours> parcours = parcoursService.getAllByDepartement(departement.getCode());
        if (parcours == null){
            return 0;
        }
        List<Parcours> parcoursList = new ArrayList<>();
        for (Parcours parcour : parcours){
            if (parcour.getActive().equals(true)){
                parcoursList.add(parcour);
            }
        }
        return parcoursList.size();
    }

    public Double statPassedByParcours(String label, int year){
        AnneeAcademique anneeAcademique = anneeAcademiqueService.getByYear(year);
        if (anneeAcademique == null){
            return 0.0;
        }
        List<Etudiant> passedList = noteService.getListPassageByParcours(label, anneeAcademique.getNumeroDebut());
        List<Etudiant> etudiantList = etudiantService.getListEtudiantByParcours(label, anneeAcademique.getNumeroDebut());
        if (passedList == null){
            return 0.0;
        }
        double result = (passedList.size()* 100)/etudiantList.size();
        return Math.round(result*100.0)/100.0;
    }

    public Double statPassedByDepartement(String code, int year){
        Departement departement = departementService.getByCode(code);
        AnneeAcademique anneeAcademique = anneeAcademiqueService.getByYear(year);
        if (departement == null || anneeAcademique == null){
            return 0.0;
        }
        List<Parcours> parcoursList = parcoursService.getAllByDepartement(departement.getCode());
        int m = parcoursList.size();
        double n = 0.0;
        for (Parcours parcours : parcoursList){
            n += statPassedByParcours(parcours.getLabel(), anneeAcademique.getNumeroDebut());
        }

        double result = n/m;
        return Math.round(result*100.0)/100.0;
    }

    public List<StatPassedDepart> statPassedAll(){
        List<Departement> departementList = departementService.getAllActif();
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueService.getAllActif();
        if (departementList == null){
            return null;
        }
        List<StatPassedDepart> statPassedDepartList = new ArrayList<>();
        for (AnneeAcademique anneeAcademique : anneeAcademiqueList){
            for (Departement departement : departementList){
                StatPassedDepart statPassedDepart = new StatPassedDepart(
                        departement.getCode(),
                        anneeAcademique.getCode(),
                        statPassedByDepartement(departement.getCode(), anneeAcademique.getNumeroDebut())
                );
                statPassedDepartList.add(statPassedDepart);
            }
        }
        return statPassedDepartList;
    }
}
