package com.api.gestnotesapi.services;

import com.api.gestnotesapi.dto.NoteDto;
import com.api.gestnotesapi.dto.PVCoursResponse;
import com.api.gestnotesapi.dto.PVCoursSansEEResponse;
import com.api.gestnotesapi.dto.PVModuleResponse;
import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.entities.Module;
import com.api.gestnotesapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<PVCoursResponse> getPVCoursByEtudiant(int session, int year, String code, String label)
    {

        List<PVCoursResponse> pvCoursResponseList = new ArrayList<>();
        Cours cours = coursRepo.findByCode(code).orElse(null);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Parcours parcours = parcoursRepo.findByLabel(label).orElse(null);

        if (cours == null || anneeAcademique == null || parcours == null) {
            return null;
        }
        List<Etudiant> etudiantList = etudiantService.getListEtudiantByParcours(parcours.getLabel(), anneeAcademique.getNumeroDebut());
        if (etudiantList == null){
            return null;
        }

        for (Etudiant etudiant : etudiantList){
            Double ccSurTrente = noteService.ccCoursSurTrente(etudiant.getId(), anneeAcademique.getNumeroDebut(), cours.getCode());
            Double moyenneSurCent = noteService.moyenneCoursSurCent(etudiant.getId(), session, anneeAcademique.getNumeroDebut(), cours.getCode());
            Double mgp = noteService.mgpPourMoyenneSurCent(moyenneSurCent);
            String grade = noteService.grade(mgp);
            Double moyenneSurVingt = 0.0;
            String decision = "";

            List<Note> noteList = noteService.getNotesEtudiantByCours(
            etudiant.getId(),
            session,
            year,
            cours.getCode()
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
            PVCoursResponse pvCoursResponse= new PVCoursResponse(cours.getCredit().getValeur(),
                    session, etudiant.getMatricule(), etudiant.getNom(), parcours.getLabel(),
                    code, cours.getIntitule(),
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

    public List<PVModuleResponse> getPVModuleByEtudiant(int year, String code, String label)
    {
        List<PVModuleResponse> pvModuleResponseList = new ArrayList<>();
        Module module = moduleRepo.findByCode(code).orElse(null);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Parcours parcours = parcoursRepo.findByLabel(label).orElse(null);

        if (module == null || anneeAcademique == null || parcours == null) {
            return null;
        }

        List<Etudiant> etudiantList = etudiantService.getListEtudiantByParcours(parcours.getLabel(), anneeAcademique.getNumeroDebut());
        if (etudiantList == null){
            return null;
        }

        for (Etudiant etudiant : etudiantList){
            Double ccSurTrente = noteService.ccModuleSurTrente(etudiant.getId(), anneeAcademique.getNumeroDebut(), module.getCode());

            List<Note> noteList = noteService.getNotesEtudiantByModule(
                    etudiant.getId(),
                    year,
                    module.getCode()
            );
            if (ccSurTrente == -1.0){
                ccSurTrente = null;
            }

            List<NoteDto> noteDtoList = new ArrayList<>();
            for (Note note : noteList){
                NoteDto noteDto = new NoteDto(note.getValeur(), note.getEvaluation().getCode());
                noteDtoList.add(noteDto);
            }
            PVModuleResponse pvModuleResponse= new PVModuleResponse(
                    module.getCode(),
                    module.getIntitule(),
                    etudiant.getMatricule(),
                    module.getCredit().getValeur(),
                    etudiant.getNom(),
                    anneeAcademique.getCode(),
                    parcours.getLabel(),
                    noteDtoList,
                    ccSurTrente
            );
            pvModuleResponseList.add(pvModuleResponse);
        }
        return pvModuleResponseList;
    }

    public List<PVCoursSansEEResponse> getPVCoursSansEEResponse(int year, String code, String label)
    {

        List<PVCoursSansEEResponse> pvCoursSansEEResponseList = new ArrayList<>();
        Cours cours = coursRepo.findByCode(code).orElse(null);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Parcours parcours = parcoursRepo.findByLabel(label).orElse(null);

        if (cours == null || anneeAcademique == null || parcours == null) {
            return null;
        }
        List<Etudiant> etudiantList = etudiantService.getListEtudiantByParcours(parcours.getLabel(), anneeAcademique.getNumeroDebut());
        if (etudiantList == null){
            return null;
        }

        for (Etudiant etudiant : etudiantList){
            Double ccSurTrente = noteService.ccCoursSurTrente(etudiant.getId(), anneeAcademique.getNumeroDebut(), cours.getCode());

            List<Note> noteList = noteService.getNotesEtudiantByCours(
                    etudiant.getId(),
                    0,
                    year,
                    cours.getCode()
            );
            if (ccSurTrente == -1.0){
                ccSurTrente = null;
            }

            List<NoteDto> noteDtoList = new ArrayList<>();
            for (Note note : noteList){
                NoteDto noteDto = new NoteDto(note.getValeur(), note.getEvaluation().getCode());
                noteDtoList.add(noteDto);
            }
            PVCoursSansEEResponse pvCoursSansEEResponse= new PVCoursSansEEResponse(
                    ccSurTrente,
                    etudiant.getMatricule(),
                    etudiant.getNom(),
                    parcours.getLabel(),
                    cours.getCode(),
                    cours.getIntitule(),
                    cours.getCredit().getValeur(),
                    anneeAcademique.getCode(),
                    noteDtoList
            );
            pvCoursSansEEResponseList.add(pvCoursSansEEResponse);
        }

        return pvCoursSansEEResponseList;

    }
}