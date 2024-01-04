package com.api.gestnotesapi.servicesImpl;

import com.api.gestnotesapi.services.MatriculeService;
import org.jvnet.hk2.annotations.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MatriculeServiceImpl implements MatriculeService {
    @Override
    public String matriculeGenerator(Long id) {

        LocalDate date = LocalDate.now();
        int year = date.getYear();
        int numYear = year - 2000;

        if (Long.toString(id).length() == 1){
            return numYear + "Y00" + id + "EB";
        }else if (Long.toString(id).length() == 2){
            return numYear + "Y0" + id + "EB";
        }else {
            return numYear + "Y" + id + "EB";
        }
    }
}
