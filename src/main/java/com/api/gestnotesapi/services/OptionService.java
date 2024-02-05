package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.Departement;
import com.api.gestnotesapi.entities.Option;
import com.api.gestnotesapi.repository.OptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OptionService {

    private OptionRepo optionRepo;
    private DepartementService departementService;

    @Autowired
    public OptionService(OptionRepo optionRepo, DepartementService departementService) {
        this.optionRepo = optionRepo;
        this.departementService = departementService;
    }

    public Option getById(Long id){
        Option option = optionRepo.findById(id).orElse(null);
        if (option == null){
            return null;
        }
        return option;
    }

    public Option addOption(Option option) {
        if (option == null){
            return null;
        }
        return optionRepo.save(option);
    }

    public List<Option> getAll() {
        List<Option> list = optionRepo.findAll();
        if (list == null){
            return null;
        }
        return list;
    }

    public List<Option> getListOptionByDepartement(String codeDepart) {
        Departement departement = departementService.getByCode(codeDepart);
        if (departement == null){
            return null;
        }
        List<Option> optionList = optionRepo.findAllByDepartement(departement);
        if (optionList == null){
            return null;
        }
        return optionList;

    }

    public Option update(Long id, Option option) {
        Option update = optionRepo.findById(id).orElse(null);
        if (update == null){
            return null;
        }
        update.setCode(option.getCode());
        update.setDescriptionFrench(option.getDescriptionFrench());
        update.setDescriptionEnglish(option.getDescriptionEnglish());

        return optionRepo.save(update);
    }

    public void delete(Long id) {
        optionRepo.deleteById(id);
    }

    public Option getByCode(String code) {
        Option option = optionRepo.findByCode(code).orElse(null);
        if (option == null){
            return null;
        }
        return option;
    }
}
