package com.api.gestnotesapi.servicesImpl;

import com.api.gestnotesapi.entities.Etudiant;
import com.api.gestnotesapi.entities.Inscription;
import com.api.gestnotesapi.entities.Parcours;
import com.api.gestnotesapi.repository.EtudiantRepo;
import com.api.gestnotesapi.repository.InscriptionRepo;
import com.api.gestnotesapi.repository.ParcoursRepo;
import com.api.gestnotesapi.services.EtudiantService;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EtudiantServiceImpl {

    @Autowired
    private ParcoursRepo parcoursRepo;
    @Autowired
    private EtudiantRepo etudiantRepo;
    @Autowired
    private InscriptionRepo inscriptionRepo;

//    public EtudiantServiceImpl(ParcoursRepo parcoursRepo, EtudiantRepo etudiantRepo, InscriptionRepo inscriptionRepo) {
//        this.parcoursRepo = parcoursRepo;
//        this.etudiantRepo = etudiantRepo;
//        this.inscriptionRepo = inscriptionRepo;
//    }

//    @Override
    public List<Etudiant> getEtudiantByParcours(String label) {
        List<Etudiant> etudiantList = new ArrayList<>();
        Optional<Parcours> parcours = parcoursRepo.findByLabel(label);
        if (!parcours.isPresent()){
            return null;
        }
        for (Inscription inscription : inscriptionRepo.findAll()){
            Parcours parcour = parcoursRepo.findById(inscription.getParcours().getId()).get();
            if (parcour.getLabel().equals(parcours.get().getLabel())){
                etudiantList.add(inscription.getEtudiant());
            }
        }
        return etudiantList;
    }
}
