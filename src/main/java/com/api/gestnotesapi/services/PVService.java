package com.api.gestnotesapi.services;

import com.api.gestnotesapi.dto.NoteDto;
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

    public List<PVCoursResponse> getPVCoursByEtudiant(
            int session, int year, String code, String label
    ) {

        List<PVCoursResponse> pvCoursResponseList = new ArrayList<>();
        Optional<Cours> cours = coursRepo.findByCode(code);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Optional<Parcours> parcours = parcoursRepo.findByLabel(label);
        if (cours.isEmpty() || anneeAcademique == null || parcours.isEmpty()) {
            return null;
        }
        List<Etudiant> etudiantList = etudiantService.getListEtudiantByParcours(parcours.get().getLabel(), anneeAcademique.getNumeroDebut());
        if (etudiantList == null){
            return null;
        }

        for (Etudiant etudiant : etudiantList){
            Double ccSurTrente = noteService.ccCoursSurTrente(etudiant.getId(), anneeAcademique.getNumeroDebut(), cours.get().getCode());
            Double moyenneSurCent = noteService.moyenneCoursSurCent(etudiant.getId(), session, anneeAcademique.getNumeroDebut(), cours.get().getCode());
            Double mgp = noteService.mgpPourMoyenneSurCent(moyenneSurCent);
            String grade = noteService.grade(mgp);
            Double moyenneSurVingt = 0.0;
            String decision = "";

                    List<Note> noteList = noteService.getNotesEtudiantByCours(
                    etudiant.getId(),
                    session,
                    year,
                    cours.get().getCode()
            );
            if (ccSurTrente == -1.0){
                ccSurTrente = null;
            }
            if (moyenneSurCent == -1.0){
                moyenneSurCent = null;
                moyenneSurVingt = null;
                decision = noteService.decision(-1.0);
            }else {
                moyenneSurVingt = noteService.convertirSurVingt(moyenneSurCent);
                decision = noteService.decision(moyenneSurVingt);
            }

            List<NoteDto> noteDtoList = new ArrayList<>();
            for (Note note : noteList){
                NoteDto noteDto = new NoteDto(note.getValeur(), note.getEvaluation().getCode());
                noteDtoList.add(noteDto);
            }
            PVCoursResponse pvCoursResponse= new PVCoursResponse(cours.get().getCredit().getValeur(),
                    session, etudiant.getMatricule(), etudiant.getNom(), parcours.get().getLabel(),
                    code, cours.get().getIntitule(),
                    anneeAcademique.getCode(),
                    noteDtoList,
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