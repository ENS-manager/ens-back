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

import java.util.ArrayList;
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
    private EtudiantService etudiantService;

    @Autowired
    public PVService(CoursRepo coursRepo, AnneeAcademiqueRepo anneeAcademiqueRepo, EtudiantRepo etudiantRepo, ParcoursRepo parcoursRepo, NoteService noteService, ModuleRepo moduleRepo, EtudiantService etudiantService) {
        this.coursRepo = coursRepo;
        this.anneeAcademiqueRepo = anneeAcademiqueRepo;
        this.etudiantRepo = etudiantRepo;
        this.parcoursRepo = parcoursRepo;
        this.noteService = noteService;
        this.moduleRepo = moduleRepo;
        this.etudiantService = etudiantService;
    }

    public List<PVCoursResponse> getPVCoursByEtudiant(PVCoursRequest pvCoursRequest) {

        List<PVCoursResponse> pvCoursResponseList = new ArrayList<>();
        Optional<Cours> cours = coursRepo.findByCode(pvCoursRequest.getCode());
        Optional<AnneeAcademique> anneeAcademique = anneeAcademiqueRepo.findByCode(pvCoursRequest.getAnneeAca());
        Optional<Parcours> parcours = parcoursRepo.findByLabel(pvCoursRequest.getParcours());
        if (cours.isEmpty() || anneeAcademique.isEmpty() || parcours.isEmpty()) {
            return null;
        }
        List<Etudiant> etudiantList = etudiantService.getListEtudiantByParcours(parcours.get().getLabel(), anneeAcademique.get().getNumeroDebut(), pvCoursRequest.getType());
        if (etudiantList.isEmpty()){
            return null;
        }

        for (Etudiant etudiant : etudiantList){
            Double ccSurTrente = noteService.ccCoursSurTrente(etudiant.getId(), anneeAcademique.get().getNumeroDebut(), cours.get().getCode());
            Double moyenneSurCent = noteService.moyenneCoursSurCent(etudiant.getId(), pvCoursRequest.getSession(), anneeAcademique.get().getNumeroDebut(), cours.get().getCode());
            Double moyenneSurVingt = noteService.convertirSurVingt(moyenneSurCent);
            String decision = noteService.decision(moyenneSurVingt);
            Double mgp = noteService.mgpPourMoyenneSurCent(moyenneSurCent);
            String grade = noteService.grade(mgp);

            List<Note> noteList = noteService.getNotesEtudiantByCours(
                    etudiant.getId(),
                    pvCoursRequest.getSession(),
                    anneeAcademique.get().getNumeroDebut(),
                    cours.get().getCode()
            );
            PVCoursResponse pvCoursResponse= new PVCoursResponse(cours.get(),
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
            pvCoursResponseList.add(pvCoursResponse);
        }

        return pvCoursResponseList;

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