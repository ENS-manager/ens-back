package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.Cours;
import com.api.gestnotesapi.entities.Niveau;
import com.api.gestnotesapi.entities.Parcours;
import com.api.gestnotesapi.entities.Semestre;
import com.api.gestnotesapi.repository.CoursRepo;
import com.api.gestnotesapi.repository.NiveauRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NiveauService {

    private NiveauRepo niveauRepo;
    private SemestreService semestreService;
    private ParcoursService parcoursService;
    private CoursRepo coursRepo;

    @Autowired
    public NiveauService(NiveauRepo niveauRepo, SemestreService semestreService, ParcoursService parcoursService, CoursRepo coursRepo) {
        this.niveauRepo = niveauRepo;
        this.semestreService = semestreService;
        this.parcoursService = parcoursService;
        this.coursRepo = coursRepo;
    }

    public Niveau addNiveau(Niveau niveau) {
        if (niveau == null){
            return null;
        }
        Boolean terminal = false;
        if (niveau.getValeur().equals(3) || niveau.getValeur().equals(5)){
            terminal = true;
        }
        List<Semestre> semestres = new ArrayList<>();
        for (Semestre semestre : semestreService.getAll()){
            semestres.add(semestre);
        }
        Niveau newNiveau = new Niveau();
        newNiveau.setCycle(niveau.getCycle());
        newNiveau.setValeur(niveau.getValeur());
        newNiveau.setTerminal(terminal);
        newNiveau.getSemestres().addAll(semestres
                .stream().
                map(semestre -> {
                    Semestre newSemestre = semestreService.getById(semestre.getId());
                    newSemestre.getNiveau().add(newNiveau);
                    return newSemestre;
                }).collect(Collectors.toList()));
        return niveauRepo.save(newNiveau);
    }

    public List<Niveau> getAll() {
        List<Niveau> niveauList = niveauRepo.findAll();
        if (niveauList == null){
            return null;
        }
        return niveauList;
    }

    public Niveau getById(Long id) {
        Niveau niveau = niveauRepo.findById(id).orElse(null);
        if (niveau == null){
            return null;
        }
        return niveau;
    }

    public Niveau update(Long id, Niveau niveau) {
        Niveau niveauFromDb = getById(id);
        if (niveauFromDb == null){
            return null;
        }
        niveauFromDb.setTerminal(niveau.getTerminal());
        niveauFromDb.setValeur(niveau.getValeur());
        niveauFromDb.setCycle(niveau.getCycle());

        return niveauRepo.save(niveauFromDb);
    }

    public String delete(Long id) {
        Niveau niveau = getById(id);
        if (niveau == null){
            return "Aucun objet trouve pour l'id specifie";
        }
        niveau.setActive(false);
        niveauRepo.save(niveau);

        return "Operation reussi avec succes";
    }

    public Niveau getByValeur(int level) {
        Niveau niveau = niveauRepo.findByValeur(level).orElse(null);
        if (niveau == null){
            return null;
        }
        return niveau;
    }

    public Niveau getNiveauOfCours(String code){
        Cours cours = coursRepo.findByCode(code).orElse(null);
        if (cours == null){
            return null;
        }
        Parcours parcours = parcoursService.getOneParcoursOfCours(cours.getCode());
        if (parcours == null){
            return null;
        }
        Niveau niveau = niveauRepo.findById(parcours.getNiveau().getId()).orElse(null);
        return niveau;
    }
}
