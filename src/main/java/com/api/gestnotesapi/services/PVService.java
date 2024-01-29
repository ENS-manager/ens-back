package com.api.gestnotesapi.services;

import com.api.gestnotesapi.dto.PVCoursRequest;
import com.api.gestnotesapi.dto.PVCoursResponse;
import com.api.gestnotesapi.dto.PVModuleRequest;
import com.api.gestnotesapi.dto.PVModuleResponse;
import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.entities.Module;
import com.api.gestnotesapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PVService {

    private CoursRepo coursRepo;
    private AnneeAcademiqueRepo anneeAcademiqueRepo;
    private EtudiantRepo etudiantRepo;
    private ParcoursRepo parcoursRepo;
    private NoteService noteService;
    private ModuleRepo moduleRepo;

    @Autowired
    public PVService(CoursRepo coursRepo, AnneeAcademiqueRepo anneeAcademiqueRepo, EtudiantRepo etudiantRepo, ParcoursRepo parcoursRepo, NoteService noteService, ModuleRepo moduleRepo) {
        this.coursRepo = coursRepo;
        this.anneeAcademiqueRepo = anneeAcademiqueRepo;
        this.etudiantRepo = etudiantRepo;
        this.parcoursRepo = parcoursRepo;
        this.noteService = noteService;
        this.moduleRepo = moduleRepo;
    }

    public PVCoursResponse getPVCoursByEtudiant(PVCoursRequest pvCoursRequest) {

        Optional<Cours> cours = coursRepo.findByCode(pvCoursRequest.getCode());
        Optional<AnneeAcademique> anneeAcademique = anneeAcademiqueRepo.findByCode(pvCoursRequest.getAnneeAca());
        Optional<Etudiant> etudiant = etudiantRepo.findByMatricule(pvCoursRequest.getMatricule());
        Optional<Parcours> parcours = parcoursRepo.findByLabel(pvCoursRequest.getParcours());

        if (cours.isEmpty() || anneeAcademique.isEmpty() || etudiant.isEmpty() || parcours.isEmpty()) {
            return null;
        }

        Double ccSurTrente = noteService.ccCoursSurTrente(etudiant.get().getId(), anneeAcademique.get().getNumeroDebut(), cours.get().getCode());
        Double moyenneSurCent = noteService.moyenneCoursSurCent(etudiant.get().getId(), pvCoursRequest.getSession(), anneeAcademique.get().getNumeroDebut(), cours.get().getCode());
        Double moyenneSurVingt = noteService.convertirSurVingt(moyenneSurCent);
        String decision = noteService.decision(moyenneSurVingt);
        Double mgp = noteService.mgpPourMoyenneSurCent(moyenneSurCent);
        String grade = noteService.grade(mgp);

        List<Note> noteList = noteService.getNotesEtudiantByCours(
                etudiant.get().getId(),
                pvCoursRequest.getSession(),
                anneeAcademique.get().getNumeroDebut(),
                cours.get().getCode()
        );

        return new PVCoursResponse(cours.get(),
                anneeAcademique.get(),
                parcours.get(),
                noteList,
                ccSurTrente,
                moyenneSurCent,
                moyenneSurVingt,
                decision,
                mgp,
                grade
        );

    }

    public PVModuleResponse getPVModuleByEtudiant(PVModuleRequest pvModuleRequest) {

        Optional<Module> module = moduleRepo.findByCode(pvModuleRequest.getCode());
        Optional<AnneeAcademique> anneeAcademique = anneeAcademiqueRepo.findByCode(pvModuleRequest.getAnneeAca());
        Optional<Etudiant> etudiant = etudiantRepo.findByMatricule(pvModuleRequest.getMatricule());
        Optional<Parcours> parcours = parcoursRepo.findByLabel(pvModuleRequest.getParcours());

        if (module.isEmpty() || anneeAcademique.isEmpty() || etudiant.isEmpty() || parcours.isEmpty()) {
            return null;
        }

        Double ccSurTrente = noteService.ccModuleSurTrente(etudiant.get().getId(), anneeAcademique.get().getNumeroDebut(), module.get().getCode());
        List<Note> noteList = noteService.getNotesEtudiantByModule(
                etudiant.get().getId(),
                anneeAcademique.get().getNumeroDebut(),
                module.get().getCode()
        );

        return new PVModuleResponse(module.get(),
                anneeAcademique.get(),
                parcours.get(),
                noteList,
                ccSurTrente
        );

    }
}