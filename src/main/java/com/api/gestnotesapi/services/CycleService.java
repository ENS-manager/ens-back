package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.Cycle;
import com.api.gestnotesapi.repository.CycleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CycleService {

    private CycleRepo cycleRepo;

    @Autowired
    public CycleService(CycleRepo cycleRepo) {
        this.cycleRepo = cycleRepo;
    }

    public List<Cycle> getAll() {
        List<Cycle> list = cycleRepo.findAll();
        if (list == null){
            return null;
        }
        return list;
    }

    public Cycle addCycle(Cycle cycle) {
        if (cycle == null){
            return null;
        }
        return cycleRepo.save(cycle);
    }

    public Cycle getById(Long id) {
        Cycle cycle = cycleRepo.findById(id).orElse(null);
        if (cycle == null){
            return null;
        }
        return cycle;
    }

    public Cycle updateById(Long id, Cycle cycle) {
        Cycle update = cycleRepo.findById(id).orElse(null);
        if (update == null){
            return null;
        }
        update.setEstAffichable(cycle.getEstAffichable());
        update.setValeur(cycle.getValeur());
        return cycleRepo.save(update);
    }

    public void delete(Long id) {
        cycleRepo.deleteById(id);
    }
}
