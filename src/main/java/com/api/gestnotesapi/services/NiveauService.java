package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.Niveau;
import com.api.gestnotesapi.repository.NiveauRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NiveauService {

    private NiveauRepo niveauRepo;

    @Autowired
    public NiveauService(NiveauRepo niveauRepo) {
        this.niveauRepo = niveauRepo;
    }

    public Niveau addNiveau(Niveau niveau) {
        if (niveau == null){
            return null;
        }
        return niveauRepo.save(niveau);
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

    public void delete(Long id) {
        niveauRepo.deleteById(id);
    }

    public Niveau getByValeur(int level) {
        Niveau niveau = niveauRepo.findByValeur(level).orElse(null);
        if (niveau == null){
            return null;
        }
        return niveau;
    }
}
