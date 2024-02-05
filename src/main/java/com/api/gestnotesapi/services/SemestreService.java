package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.Semestre;
import com.api.gestnotesapi.repository.SemestreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemestreService {

    private SemestreRepo semestreRepo;

    @Autowired
    public SemestreService(SemestreRepo semestreRepo) {
        this.semestreRepo = semestreRepo;
    }

    public Semestre getValeur(int semestre) {
        Semestre sem = semestreRepo.findByValeur(semestre);
        if (sem == null){
            return null;
        }
        return sem;
    }

    public Semestre addService(Semestre semestre) {
        if (semestre == null){
            return null;
        }
        return semestreRepo.save(semestre);
    }

    public List<Semestre> getAll() {
        List<Semestre> list = semestreRepo.findAll();
        if (list == null){
            return null;
        }
        return list;
    }

    public Semestre getById(Long id) {
        Semestre semestre = semestreRepo.findById(id).orElse(null);
        if (semestre == null){
            return null;
        }
        return semestre;
    }

    public Semestre update(Long id, Semestre semestre) {
        Semestre update = getById(id);
        if (update == null){
            return null;
        }
        update.setValeur(semestre.getValeur());
        update.setNiveau(semestre.getNiveau());

        return semestreRepo.save(update);
    }

    public void delete(Long id) {
        semestreRepo.deleteById(id);
    }
}
