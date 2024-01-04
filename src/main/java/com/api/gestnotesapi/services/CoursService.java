package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.Cours;
import org.jvnet.hk2.annotations.Service;

import java.util.Optional;

public interface CoursService {
    Optional<Cours> getCoursByCode(String code);
}
