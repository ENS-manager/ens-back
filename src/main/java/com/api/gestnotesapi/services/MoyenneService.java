package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.AnneeAcademique;
import com.api.gestnotesapi.entities.Cours;
import com.api.gestnotesapi.entities.Etudiant;
import com.api.gestnotesapi.entities.Moyenne;
import com.api.gestnotesapi.repository.MoyenneRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoyenneService {

    private MoyenneRepo moyenneRepo;
    private EtudiantService etudiantService;
    private CoursService coursService;
    private AnneeAcademiqueService anneeAcademiqueService;

    @Autowired
    public MoyenneService(MoyenneRepo moyenneRepo, EtudiantService etudiantService, CoursService coursService, AnneeAcademiqueService anneeAcademiqueService) {
        this.moyenneRepo = moyenneRepo;
        this.etudiantService = etudiantService;
        this.coursService = coursService;
        this.anneeAcademiqueService = anneeAcademiqueService;
    }

    public Moyenne addMoyenne(Moyenne moyenne){
        if (moyenne == null){
            return null;
        }
        return moyenneRepo.save(moyenne);
    }

    public Moyenne getById(Long id){
        Moyenne moyenne = moyenneRepo.findById(id).orElse(null);
        if (moyenne == null){
            return null;
        }
        return moyenne;
    }

    public Moyenne updateMoyenne(Long id, Moyenne moyenne) {
        Moyenne moyenneFromDB = getById(id);
        if (moyenneFromDB == null){
            return null;
        }
        moyenneFromDB.setValeur(moyenne.getValeur());
        moyenneFromDB.setSession(moyenne.getSession());
        moyenneFromDB.setEtudiant(moyenne.getEtudiant());
        moyenneFromDB.setCours(moyenne.getCours());
        moyenneFromDB.setAnneeAcademique(moyenne.getAnneeAcademique());

        return moyenneRepo.save(moyenneFromDB);
    }

    public Moyenne getLastMoyenneCoursFromEtudiant(Long id, String code){
        Moyenne result = new Moyenne();
        Etudiant etudiant = etudiantService.getById(id);
        Cours cours = coursService.getByCode(code);
        if (etudiant == null || cours == null){
            return null;
        }
        List<Moyenne> moyenneList = moyenneRepo.findAllByEtudiantAndCours(etudiant, cours);
        if (moyenneList == null){
            return null;
        }
        int year = 0;
        for (Moyenne moyenne : moyenneList){
            if (moyenne.getAnneeAcademique().getNumeroDebut() > year){
                year = moyenne.getAnneeAcademique().getNumeroDebut();
            }
        }
        AnneeAcademique anneeAcademique = anneeAcademiqueService.getByYear(year);
        List<Moyenne> moyennes = moyenneRepo.findAllByEtudiantAndCoursAndAnneeAcademique(etudiant, cours, anneeAcademique);
        if (moyennes.size() > 1){
            for (Moyenne moyenne : moyennes){
                if (moyenne.getSession().equals(2)){
                    result = moyenne;
                }
            }
        }else if (moyennes.size() == 1){
            result = moyennes.get(0);
        }else {
            result = null;
        }
        return result;
    }

    public Moyenne getNoteStageFromEtudiant(Long id, String code){
        Etudiant etudiant = etudiantService.getById(id);
        Cours cours = coursService.getByCode(code);
        if (etudiant == null || cours == null){
            return null;
        }
        Moyenne moyenne = getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode());
        return moyenne;
    }

//    public Boolean isExist(Moyenne moyenne){
//        if (moyenneRepo.existsById(moyenne.getId())){
//            return true;
//        }
//        return false;
//    }
    public Moyenne getMoyenneByEtudiantAndAnneeAndCoursAndSessionAndValeur(Long id, int year, String code, int session, Double valeur){
        Etudiant etudiant = etudiantService.getById(id);
        AnneeAcademique anneeAcademique = anneeAcademiqueService.getByYear(year);
        Cours cours = coursService.getByCode(code);
        if (etudiant == null || anneeAcademique == null || cours == null){
            return null;
        }
        return moyenneRepo.findAllByEtudiantAndCoursAndAnneeAcademiqueAndSessionAndValeur(etudiant, cours, anneeAcademique, session, valeur);
    }
}
