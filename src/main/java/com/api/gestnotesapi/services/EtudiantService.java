package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.Etudiant;
import org.jvnet.hk2.annotations.Service;

import java.util.List;

public interface EtudiantService {

   List<Etudiant> getEtudiantByParcours(String label);
}
