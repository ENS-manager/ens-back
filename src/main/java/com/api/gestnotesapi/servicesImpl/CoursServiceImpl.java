package com.api.gestnotesapi.servicesImpl;

import com.api.gestnotesapi.entities.Cours;
import com.api.gestnotesapi.repository.CoursRepo;
import com.api.gestnotesapi.services.CoursService;
import org.jvnet.hk2.annotations.Service;

import java.util.Optional;

@Service
public class CoursServiceImpl implements CoursService {

//    @Autowired
    private CoursRepo coursRepo;

    public CoursServiceImpl(CoursRepo coursRepo) {
        this.coursRepo = coursRepo;
    }

    @Override
    public Optional<Cours> getCoursByCode(String code) {

        return coursRepo.findByCode(code);
    }
}
