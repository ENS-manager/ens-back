package com.api.gestnotesapi.services;

import com.api.gestnotesapi.dto.AnonymatDto;
import com.api.gestnotesapi.dto.AnonymatResponse;
import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.repository.AnneeAcademiqueRepo;
import com.api.gestnotesapi.repository.AnonymatRepo;
import com.api.gestnotesapi.repository.CoursRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnonymatService{

    private AnonymatRepo anonymatRepo;
    private CoursRepo coursRepo;
    private AnneeAcademiqueRepo anneeAcademiqueRepo;
    private EtudiantService etudiantService;
    private ParcoursService parcoursService;

    @Autowired
    public AnonymatService(AnonymatRepo anonymatRepo, CoursRepo coursRepo, AnneeAcademiqueRepo anneeAcademiqueRepo, EtudiantService etudiantService, ParcoursService parcoursService) {
        this.anonymatRepo = anonymatRepo;
        this.coursRepo = coursRepo;
        this.anneeAcademiqueRepo = anneeAcademiqueRepo;
        this.etudiantService = etudiantService;
        this.parcoursService = parcoursService;
    }

    public String anonymatGenerator(String codeUE, int session, int n) {
        int annee = LocalDate.now().getYear();
        int year = annee-2000;
        return String.valueOf(codeUE + session + year + "_" + n);
    }

    public Anonymat addAnonymat(Anonymat anonymat, int n) {

        if (anonymat == null){
            throw new RuntimeException("Remplissez les champs");
        }
        int session = 0;
        if (anonymat.getSessions().equals(null)){
            session = 1;
        }else {
            session = anonymat.getSessions();
        }
        Cours cours = coursRepo.findByCoursId(anonymat.getCours().getCoursId());

        String valeur = "";
        if (anonymat.getValeur() == null){
            valeur = anonymatGenerator(cours.getCode(), session, n);
            anonymat.setValeur(valeur);
            return anonymatRepo.save(anonymat);
        }
        return anonymatRepo.save(anonymat);
    }

    public AnonymatResponse getAnonymatCours(int session, int year, String code) {
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Cours cours = coursRepo.findByCode(code).orElse(null);
        AnonymatResponse anonymatResponse = new AnonymatResponse();
        if (anneeAcademique == null || cours == null){
            throw new RuntimeException("Aucune donnée trouvée pour les valeurs specifiées");
        }
        List<Anonymat> anonymatList = anonymatRepo.findAllByAnneeAcademiqueAndCoursAndSessions(anneeAcademique, cours, session);
        if (anonymatList == null){
            throw new RuntimeException("Aucune donnée trouvée pour les valeurs specifiées");
        }
        Anonymat anonym = anonymatList.get(0);
        Parcours parcours = parcoursService.getParcoursEtudiant(anonym.getEtudiant().getId(), anneeAcademique.getCode());
        List<AnonymatDto> anonymatDtoList = new ArrayList<>();
        for (Anonymat anonymat : anonymatList){
            AnonymatDto anonymatDto = new AnonymatDto();
            anonymatDto.setAnonymat(anonymat.getValeur());
            anonymatDto.setMatricule(anonymat.getEtudiant().getMatricule());

            anonymatDtoList.add(anonymatDto);
        }
        anonymatResponse.setCours(cours.getCode());
        anonymatResponse.setAnneeAca(anneeAcademique.getCode());
        anonymatResponse.setParcours(parcours.getLabel());
        anonymatResponse.setSession(session);
        anonymatResponse.setAnonymatDtoList(anonymatDtoList);
        return anonymatResponse;
    }

    public Anonymat getById(Long id) {
        Anonymat anonymat = anonymatRepo.findById(id).orElse(null);
        if (anonymat == null){
            throw new RuntimeException("Aucune donnée trouvée pour l'id: "+ id);
        }
        return anonymat;
    }

    public String delete(Long id) {
        Anonymat anonymat = getById(id);
        if (anonymat == null){
            return "Aucun objet trouve pour l'id specifie";
        }
        anonymat.setActive(false);
        anonymatRepo.save(anonymat);

        return "Operation reussi avec succes";
    }

    public Anonymat getByValeur(String valeur) {
        Anonymat anonymat = anonymatRepo.findByValeur(valeur);
        if (anonymat == null){
            throw new RuntimeException("Aucune donnée trouvée pour l'anonymat: "+ valeur);
        }
        return anonymat;
    }
}
