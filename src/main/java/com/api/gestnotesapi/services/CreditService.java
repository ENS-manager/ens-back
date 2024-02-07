package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.AnneeAcademique;
import com.api.gestnotesapi.entities.Credit;
import com.api.gestnotesapi.repository.CreditRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditService {

    private CreditRepo creditRepo;

    @Autowired
    public CreditService(CreditRepo creditRepo) {
        this.creditRepo = creditRepo;
    }


    public Credit addCredit(Credit credit) {
        if (credit == null){
            return null;
        }
        return creditRepo.save(credit);
    }

    public List<Credit> getAll() {
        List<Credit> list = creditRepo.findAll();
        if (list == null){
            return null;
        }
        return list;
    }

    public Credit getById(Long id) {
        Credit credit = creditRepo.findById(id).orElse(null);
        if (credit == null){
            return null;
        }
        return credit;
    }

    public Credit updateById(Long id, Credit credit) {
        Credit update = creditRepo.findById(id).orElse(null);
        if (update == null){
            return null;
        }
        update.setValeur(credit.getValeur());
        return creditRepo.save(update);
    }

    public String delete(Long id) {
        Credit credit = getById(id);
        if (credit == null){
            return "Aucun objet trouve pour l'id specifie";
        }
        credit.setActive(false);
        creditRepo.save(credit);

        return "Operation reussi avec succes";
    }
}
