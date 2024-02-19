package com.api.gestnotesapi.services;

import com.api.gestnotesapi.dto.*;
import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.entities.Module;
import com.api.gestnotesapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private NiveauRepo niveauRepo;
    private CycleRepo cycleRepo;
    private CoursService coursService;
    private NiveauService niveauService;
    private MoyenneService moyenneService;

    @Autowired
    public PVService(CoursRepo coursRepo, AnneeAcademiqueRepo anneeAcademiqueRepo, EtudiantRepo etudiantRepo, ParcoursRepo parcoursRepo, NoteService noteService, ModuleRepo moduleRepo, EtudiantService etudiantService, NiveauRepo niveauRepo, CycleRepo cycleRepo, CoursService coursService, NiveauService niveauService, MoyenneService moyenneService) {
        this.coursRepo = coursRepo;
        this.anneeAcademiqueRepo = anneeAcademiqueRepo;
        this.etudiantRepo = etudiantRepo;
        this.parcoursRepo = parcoursRepo;
        this.noteService = noteService;
        this.moduleRepo = moduleRepo;
        this.etudiantService = etudiantService;
        this.niveauRepo = niveauRepo;
        this.cycleRepo = cycleRepo;
        this.coursService = coursService;
        this.niveauService = niveauService;
        this.moyenneService = moyenneService;
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
        if (haveAmodule(cours.getCode()) == true){
            noteService.caculMoyennePondere(year, code);
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

//        pour le pv grand Jury
    public List<PVGrandJuryResponse> getPVGrandJury(String code, int cycle, LocalDate session){
        List <PVGrandJuryResponse> pvGrandJuryResponseList = new ArrayList<>();
        int year = session.getYear();
        int temp = year - 3;
        String promo = temp+"-"+year;
        String sessions = session.getMonth().name()+" "+session.getYear();
        if ( cycle == 1){
//            niveau 1
            double moyf11 = 0.0;
            double moyf12 = 0.0;
            double moyc11 = 0.0;
            double moyc12 = 0.0;
            double moyp11 = 0.0;
            double moyp12 = 0.0;
//            niveau 2
            double moyf21 = 0.0;
            double moyf22 = 0.0;
            double moyc21 = 0.0;
            double moyc22 = 0.0;
            double moyp21 = 0.0;
            double moyp22 = 0.0;
//            niveau 3
            double moyf31 = 0.0;
            double moyp31 = 0.0;
            double moyc31 = 0.0;

            String label = code+" "+3;
            int val = year - 1;
            AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(val);
            List<Cours> coursList = coursService.getListCoursByOptionAndCycle(code, cycle);
            List<Etudiant> etudiantList = etudiantService.getListEtudiantByParcours(label, val);
            if (coursList == null || etudiantList == null || anneeAcademique == null){
                return null;
            }
            for (Etudiant etudiant : etudiantList){
                PVGrandJuryResponse pvGrandJuryResponse = new PVGrandJuryResponse();
                List<PVGrandJuryDto> pvGrandJuryDtoList = new ArrayList<>();
                PVGrandJuryDto pvGrandJuryDto = new PVGrandJuryDto();
                pvGrandJuryDto.setDateDeNaissance(etudiant.getDateDeNaissance());
                pvGrandJuryDto.setLieuDeNaissance(etudiant.getLieuDeNaissance());
                pvGrandJuryDto.setMatricule(etudiant.getMatricule());
                pvGrandJuryDto.setNom(etudiant.getNom());
                List<MoyenneCours> moyenneCoursList = new ArrayList<>();
                int nbFond11 = 0;
                int nbFond12 = 0;
                int nbFond21 = 0;
                int nbFond22 = 0;
                int nbFond31 = 0;
                int nbComp11 = 0;
                int nbComp12 = 0;
                int nbComp21 = 0;
                int nbComp22 = 0;
                int nbComp31 = 0;
                int nbProf11 = 0;
                int nbProf12 = 0;
                int nbProf21 = 0;
                int nbProf22 = 0;
                int nbProf31 = 0;

                for (Cours cours : coursList){
                    Niveau niveau = niveauService.getNiveauOfCours(cours.getCode());
                    if (niveau == null){
                        return null;
                    }
                    if ((cours.getSemestre().getValeur().equals(1))
                            && (niveau.getValeur().equals(1))
                            && (cours.getNatureUE().equals(NatureUE.Fondamentale))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyf11 += 10;
                            nbFond11++;
                        }else {
                            moyf11 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbFond11++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(2))
                            && (niveau.getValeur().equals(1))
                            && (cours.getNatureUE().equals(NatureUE.Fondamentale))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyf12 += 10;
                            nbFond12++;
                        }else{
                            moyf12 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbFond12++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(1))
                            && (niveau.getValeur().equals(1))
                            && (cours.getNatureUE().equals(NatureUE.Complementaire))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyc11 += 10;
                            nbComp11++;
                        }else {
                            moyc11 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbComp11++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(2))
                            && (niveau.getValeur().equals(1))
                            && (cours.getNatureUE().equals(NatureUE.Complementaire))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyc12 += 10;
                            nbComp12++;
                        }else {
                            moyc12 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbComp12++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(1))
                            && (niveau.getValeur().equals(1))
                            && (cours.getNatureUE().equals(NatureUE.Professionnelle))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyp11 += 10;
                            nbProf11++;
                        }else {
                            moyp11 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbProf11++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(2))
                            && (niveau.getValeur().equals(1))
                            && (cours.getNatureUE().equals(NatureUE.Professionnelle))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyp12 += 10;
                            nbComp12++;
                        }else {
                            moyp12 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbComp12++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(1))
                            && (niveau.getValeur().equals(2))
                            && (cours.getNatureUE().equals(NatureUE.Fondamentale))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyf21 += 10;
                            nbFond21++;
                        }else {
                            moyf21 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbFond21++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(2))
                            && (niveau.getValeur().equals(2))
                            && (cours.getNatureUE().equals(NatureUE.Fondamentale))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyf22 += 10;
                            nbFond22++;
                        }else {
                            moyf22 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbFond22++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(1))
                            && (niveau.getValeur().equals(2))
                            && (cours.getNatureUE().equals(NatureUE.Complementaire))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyc21 += 10;
                            nbComp21++;
                        }else {
                            moyc21 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbComp21++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(2))
                            && (niveau.getValeur().equals(2))
                            && (cours.getNatureUE().equals(NatureUE.Complementaire))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyc22 += 10;
                            nbComp22++;
                        }else {
                            moyc22 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbComp22++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(1))
                            && (niveau.getValeur().equals(2))
                            && (cours.getNatureUE().equals(NatureUE.Professionnelle))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyp21 += 10;
                            nbProf21++;
                        }else {
                            moyp21 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbProf21++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(2))
                            && (niveau.getValeur().equals(2))
                            && (cours.getNatureUE().equals(NatureUE.Professionnelle))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyp22 += 10;
                            nbProf22++;
                        }else {
                            moyp22 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbProf22++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(1))
                            && (niveau.getValeur().equals(3))
                            && (cours.getNatureUE().equals(NatureUE.Fondamentale))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyf31 += 10;
                            nbFond31++;
                        }else {
                            moyf31 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbFond31++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(1))
                            && (niveau.getValeur().equals(3))
                            && (cours.getNatureUE().equals(NatureUE.Complementaire))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyc31 += 10;
                            nbComp31++;
                        }else {
                            moyc31 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbComp31++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(1))
                            && (niveau.getValeur().equals(3))
                            && (cours.getNatureUE().equals(NatureUE.Professionnelle))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyp31 += 10;
                            nbProf31++;
                        }else {
                            moyp31 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbProf31++;
                        }
                    }
                }
                MoyenneCours moyenneCours1 = new MoyenneCours();
                moyenneCours1.setCode(coursService.generateCode(NatureUE.Fondamentale, 1, 1));
                moyenneCours1.setMoy(moyf11/nbFond11);
                moyenneCoursList.add(moyenneCours1);
                MoyenneCours moyenneCours2 = new MoyenneCours();
                moyenneCours2.setCode(coursService.generateCode(NatureUE.Fondamentale, 2, 1));
                moyenneCours2.setMoy(moyf12/nbFond12);
                moyenneCoursList.add(moyenneCours2);

                MoyenneCours moyenneCours3 = new MoyenneCours();
                moyenneCours3.setCode(coursService.generateCode(NatureUE.Complementaire, 1, 1));
                moyenneCours3.setMoy(moyc11/nbComp11);
                moyenneCoursList.add(moyenneCours3);
                MoyenneCours moyenneCours4 = new MoyenneCours();
                moyenneCours4.setCode(coursService.generateCode(NatureUE.Complementaire, 2, 1));
                moyenneCours4.setMoy(moyc12/nbComp12);
                moyenneCoursList.add(moyenneCours4);

                MoyenneCours moyenneCours5 = new MoyenneCours();
                moyenneCours5.setCode(coursService.generateCode(NatureUE.Professionnelle, 1, 1));
                moyenneCours5.setMoy(moyp11/nbProf11);
                moyenneCoursList.add(moyenneCours5);
                MoyenneCours moyenneCours6 = new MoyenneCours();
                moyenneCours6.setCode(coursService.generateCode(NatureUE.Professionnelle, 2, 1));
                moyenneCours6.setMoy(moyp12/nbProf12);
                moyenneCoursList.add(moyenneCours6);

                MoyenneCours moyenneCours7 = new MoyenneCours();
                moyenneCours7.setCode(coursService.generateCode(NatureUE.Fondamentale, 1, 2));
                moyenneCours7.setMoy(moyf21/nbFond21);
                moyenneCoursList.add(moyenneCours7);
                MoyenneCours moyenneCours8 = new MoyenneCours();
                moyenneCours8.setCode(coursService.generateCode(NatureUE.Fondamentale, 2, 2));
                moyenneCours8.setMoy(moyf22/nbFond22);
                moyenneCoursList.add(moyenneCours8);

                MoyenneCours moyenneCours9 = new MoyenneCours();
                moyenneCours9.setCode(coursService.generateCode(NatureUE.Complementaire, 1, 2));
                moyenneCours9.setMoy(moyc21/nbComp21);
                moyenneCoursList.add(moyenneCours9);
                MoyenneCours moyenneCours10 = new MoyenneCours();
                moyenneCours10.setCode(coursService.generateCode(NatureUE.Complementaire, 2, 2));
                moyenneCours10.setMoy(moyc22/nbComp22);
                moyenneCoursList.add(moyenneCours10);

                MoyenneCours moyenneCours11 = new MoyenneCours();
                moyenneCours11.setCode(coursService.generateCode(NatureUE.Professionnelle, 1, 2));
                moyenneCours11.setMoy(moyp21/nbProf21);
                moyenneCoursList.add(moyenneCours11);
                MoyenneCours moyenneCours12 = new MoyenneCours();
                moyenneCours12.setCode(coursService.generateCode(NatureUE.Professionnelle, 2, 2));
                moyenneCours12.setMoy(moyp22/nbProf22);
                moyenneCoursList.add(moyenneCours12);

                MoyenneCours moyenneCours13 = new MoyenneCours();
                moyenneCours13.setCode(coursService.generateCode(NatureUE.Fondamentale, 1, 3));
                moyenneCours13.setMoy(moyf31/nbFond31);
                moyenneCoursList.add(moyenneCours13);
                MoyenneCours moyenneCours14 = new MoyenneCours();
                moyenneCours14.setCode(coursService.generateCode(NatureUE.Complementaire, 1, 3));
                moyenneCours14.setMoy(moyc31/nbComp31);
                moyenneCoursList.add(moyenneCours14);
                MoyenneCours moyenneCours15 = new MoyenneCours();
                moyenneCours15.setCode(coursService.generateCode(NatureUE.Professionnelle, 1, 3));
                moyenneCours15.setMoy(moyp31/nbProf31);
                moyenneCoursList.add(moyenneCours15);

                Double fondamentale = (moyf11+moyf12+moyf21+moyf22+moyf31)/5;
                Double complementaire = (moyc11+moyc12+moyc21+moyc22+moyc31)/5;
                Double professionnelle = (moyp11+moyp12+moyp21+moyp22+moyp31)/5;

                Double stagePra = noteService.getNoteStagePrat(etudiant.getId(), anneeAcademique.getNumeroDebut());
                Double stage = noteService.getNoteStage(etudiant.getId(), anneeAcademique.getNumeroDebut());

                Double moyEcrite = fondamentale*0.4 + complementaire*0.1 + professionnelle*0.2 + stagePra*0.3;
//                A revoir
                Double moyGene = moyEcrite*0.8 + stagePra*0.2;

                String decision = noteService.decider(moyGene);
                String mention = noteService.mention(moyGene);

                pvGrandJuryDto.setComplementaire(complementaire);
                pvGrandJuryDto.setFondamentale(fondamentale);
                pvGrandJuryDto.setProfessionnelle(professionnelle);
                pvGrandJuryDto.setDecision(decision);
                pvGrandJuryDto.setMention(mention);
                pvGrandJuryDto.setMoyenneCours(moyenneCoursList);
                pvGrandJuryDto.setMoyenneEcrite(moyEcrite);
                pvGrandJuryDto.setMoyenneGene(moyGene);
                pvGrandJuryDto.setNoteStage(stage);
                pvGrandJuryDto.setNoteStagePra(stagePra);

                pvGrandJuryDtoList.add(pvGrandJuryDto);

                pvGrandJuryResponse.setPvGrandJuryDtoList(pvGrandJuryDtoList);
                pvGrandJuryResponse.setCycle(cycle);
                pvGrandJuryResponse.setAnneeAca(anneeAcademique.getCode());
                pvGrandJuryResponse.setPromotion(promo);
                pvGrandJuryResponse.setSession(sessions);

                pvGrandJuryResponseList.add(pvGrandJuryResponse);
            }
        }else {
//            niveau 4
            double moyf41 = 0.0;
            double moyf42 = 0.0;
            double moyc41 = 0.0;
            double moyc42 = 0.0;
            double moyp41 = 0.0;
            double moyp42 = 0.0;
//            niveau 5
            double moyf51 = 0.0;
            double moyc51 = 0.0;
            double moyp51 = 0.0;

            String label = code+" "+3;
            int val = year - 1;
            AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(val);
            List<Cours> coursList = coursService.getListCoursByOptionAndCycle(code, cycle);
            List<Etudiant> etudiantList = etudiantService.getListEtudiantByParcours(label, val);
            if (coursList == null || etudiantList == null || anneeAcademique == null){
                return null;
            }
            for (Etudiant etudiant : etudiantList){
                PVGrandJuryResponse pvGrandJuryResponse = new PVGrandJuryResponse();
                List<PVGrandJuryDto> pvGrandJuryDtoList = new ArrayList<>();
                PVGrandJuryDto pvGrandJuryDto = new PVGrandJuryDto();
                pvGrandJuryDto.setDateDeNaissance(etudiant.getDateDeNaissance());
                pvGrandJuryDto.setLieuDeNaissance(etudiant.getLieuDeNaissance());
                pvGrandJuryDto.setMatricule(etudiant.getMatricule());
                pvGrandJuryDto.setNom(etudiant.getNom());
                List<MoyenneCours> moyenneCoursList = new ArrayList<>();
                int nbFond41 = 0;
                int nbFond42 = 0;
                int nbFond51 = 0;
                int nbComp41 = 0;
                int nbComp42 = 0;
                int nbComp51 = 0;
                int nbProf41 = 0;
                int nbProf42 = 0;
                int nbProf51 = 0;

                for (Cours cours : coursList){
                    Niveau niveau = niveauService.getNiveauOfCours(cours.getCode());
                    if (niveau == null){
                        return null;
                    }
                    if ((cours.getSemestre().getValeur().equals(1))
                            && (niveau.getValeur().equals(4))
                            && (cours.getNatureUE().equals(NatureUE.Fondamentale))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyf41 += 10;
                            nbFond41++;
                        }else {
                            moyf41 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbFond41++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(2))
                            && (niveau.getValeur().equals(4))
                            && (cours.getNatureUE().equals(NatureUE.Fondamentale))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyf42 += 10;
                            nbFond42++;
                        }else{
                            moyf42 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbFond42++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(1))
                            && (niveau.getValeur().equals(4))
                            && (cours.getNatureUE().equals(NatureUE.Complementaire))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyc41 += 10;
                            nbComp41++;
                        }else {
                            moyc41 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbComp41++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(2))
                            && (niveau.getValeur().equals(4))
                            && (cours.getNatureUE().equals(NatureUE.Complementaire))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyc42 += 10;
                            nbComp42++;
                        }else {
                            moyc42 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbComp42++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(1))
                            && (niveau.getValeur().equals(4))
                            && (cours.getNatureUE().equals(NatureUE.Professionnelle))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyp41 += 10;
                            nbProf41++;
                        }else {
                            moyp41 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbProf41++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(2))
                            && (niveau.getValeur().equals(4))
                            && (cours.getNatureUE().equals(NatureUE.Professionnelle))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyp42 += 10;
                            nbComp42++;
                        }else {
                            moyp42 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbComp42++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(1))
                            && (niveau.getValeur().equals(5))
                            && (cours.getNatureUE().equals(NatureUE.Fondamentale))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyf51 += 10;
                            nbFond51++;
                        }else {
                            moyf51 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbFond51++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(1))
                            && (niveau.getValeur().equals(5))
                            && (cours.getNatureUE().equals(NatureUE.Complementaire))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyc51 += 10;
                            nbComp51++;
                        }else {
                            moyc51 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbComp51++;
                        }
                    }else if ((cours.getSemestre().getValeur().equals(1))
                            && (niveau.getValeur().equals(5))
                            && (cours.getNatureUE().equals(NatureUE.Professionnelle))){
                        if (moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur() < 10){
                            moyp51 += 10;
                            nbProf51++;
                        }else {
                            moyp51 += moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), cours.getCode()).getValeur();
                            nbProf51++;
                        }
                    }
                }
                MoyenneCours moyenneCours1 = new MoyenneCours();
                moyenneCours1.setCode(coursService.generateCode(NatureUE.Fondamentale, 1, 4));
                moyenneCours1.setMoy(moyf41/nbFond41);
                moyenneCoursList.add(moyenneCours1);
                MoyenneCours moyenneCours2 = new MoyenneCours();
                moyenneCours2.setCode(coursService.generateCode(NatureUE.Fondamentale, 2, 4));
                moyenneCours2.setMoy(moyf42/nbFond42);
                moyenneCoursList.add(moyenneCours2);

                MoyenneCours moyenneCours3 = new MoyenneCours();
                moyenneCours3.setCode(coursService.generateCode(NatureUE.Complementaire, 1, 4));
                moyenneCours3.setMoy(moyc41/nbComp41);
                moyenneCoursList.add(moyenneCours3);
                MoyenneCours moyenneCours4 = new MoyenneCours();
                moyenneCours4.setCode(coursService.generateCode(NatureUE.Complementaire, 2, 4));
                moyenneCours4.setMoy(moyc42/nbComp42);
                moyenneCoursList.add(moyenneCours4);

                MoyenneCours moyenneCours5 = new MoyenneCours();
                moyenneCours5.setCode(coursService.generateCode(NatureUE.Professionnelle, 1, 4));
                moyenneCours5.setMoy(moyp41/nbProf41);
                moyenneCoursList.add(moyenneCours5);
                MoyenneCours moyenneCours6 = new MoyenneCours();
                moyenneCours6.setCode(coursService.generateCode(NatureUE.Professionnelle, 2, 4));
                moyenneCours6.setMoy(moyp42/nbProf42);
                moyenneCoursList.add(moyenneCours6);

                MoyenneCours moyenneCours7 = new MoyenneCours();
                moyenneCours7.setCode(coursService.generateCode(NatureUE.Fondamentale, 1, 5));
                moyenneCours7.setMoy(moyf51/nbFond51);
                moyenneCoursList.add(moyenneCours7);
                MoyenneCours moyenneCours8 = new MoyenneCours();
                moyenneCours8.setCode(coursService.generateCode(NatureUE.Complementaire, 1, 5));
                moyenneCours8.setMoy(moyc51/nbComp51);
                moyenneCoursList.add(moyenneCours8);

                MoyenneCours moyenneCours9 = new MoyenneCours();
                moyenneCours9.setCode(coursService.generateCode(NatureUE.Professionnelle, 1, 5));
                moyenneCours9.setMoy(moyp51/nbProf51);
                moyenneCoursList.add(moyenneCours9);

                Double fondamentale = (moyf41+moyf42+moyf51)/5;
                Double complementaire = (moyc41+moyc42+moyc51)/5;
                Double professionnelle = (moyp41+moyp42+moyp51)/5;

                Double stagePra = moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), "StagePrat").getValeur();
                Double stage = moyenneService.getLastMoyenneCoursFromEtudiant(etudiant.getId(), "Stage").getValeur();

                Double moyEcrite = fondamentale*0.4 + complementaire*0.1 + professionnelle*0.2 + stagePra*0.3;
//                A revoir
                Double moyGene = moyEcrite*0.8 + stagePra*0.2;

                String decision = noteService.decision(moyGene);
                Double mgp = noteService.mgpPourMoyenneSurVingt(moyGene);
                String mention = noteService.grade(mgp);

                pvGrandJuryDto.setComplementaire(complementaire);
                pvGrandJuryDto.setFondamentale(fondamentale);
                pvGrandJuryDto.setProfessionnelle(professionnelle);
                pvGrandJuryDto.setDecision(decision);
                pvGrandJuryDto.setMention(mention);
                pvGrandJuryDto.setMoyenneCours(moyenneCoursList);
                pvGrandJuryDto.setMoyenneEcrite(moyEcrite);
                pvGrandJuryDto.setMoyenneGene(moyGene);
                pvGrandJuryDto.setNoteStage(stage);
                pvGrandJuryDto.setNoteStagePra(stagePra);

                pvGrandJuryDtoList.add(pvGrandJuryDto);

                pvGrandJuryResponse.setPvGrandJuryDtoList(pvGrandJuryDtoList);
                pvGrandJuryResponse.setCycle(cycle);
                pvGrandJuryResponse.setAnneeAca(anneeAcademique.getCode());
                pvGrandJuryResponse.setPromotion(promo);
                pvGrandJuryResponse.setSession(sessions);

                pvGrandJuryResponseList.add(pvGrandJuryResponse);
            }
        }
        return pvGrandJuryResponseList;
    }

    public List<PVAnnuel> getPVAnnuel(String code, String label){
        Parcours parcours = parcoursRepo.findByLabel(label).orElse(null);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByCode(code).orElse(null);
        if (parcours == null || anneeAcademique == null){
            return null;
        }
        List<Cours> coursList = coursService.getListCoursByParcours(label);
        List<Etudiant> etudiantList = etudiantService.getListEtudiantByParcours(label, anneeAcademique.getNumeroDebut());
        if (coursList == null || etudiantList == null){
            return null;
        }
        List<PVAnnuel> pvAnnuelList = new ArrayList<>();
        List<MoyenneCours> coursSem1 = new ArrayList<>();
        List<MoyenneCours> coursSem2 = new ArrayList<>();
        for (Etudiant etudiant : etudiantList){
            PVAnnuel pvAnnuel = new PVAnnuel();
            Double moySem1 = noteService.moyenneSemestre(etudiant.getId(), anneeAcademique.getNumeroDebut(), 1);
            Double moySem2 = noteService.moyenneSemestre(etudiant.getId(), anneeAcademique.getNumeroDebut(), 2);
            Integer creditSem1 = noteService.creditSemestre(etudiant.getId(), anneeAcademique.getNumeroDebut(), 1);
            Integer creditSem2 = noteService.creditSemestre(etudiant.getId(), anneeAcademique.getNumeroDebut(), 2);
            Double moyAnnuel = noteService.moyenneAnnuelle(etudiant.getId(), anneeAcademique.getNumeroDebut());
            pvAnnuel.setAnneeAca(code);
            pvAnnuel.setMatricule(etudiant.getMatricule());
            pvAnnuel.setNom(etudiant.getNom());
            pvAnnuel.setParcours(label);
            pvAnnuel.setType(etudiant.getType());
            pvAnnuel.setMoyAnnuelle(moyAnnuel);
            pvAnnuel.setMoySem1(moySem1);
            pvAnnuel.setMoySem2(moySem2);
            pvAnnuel.setCreditSem1(creditSem1);
            pvAnnuel.setCreditSem2(creditSem2);
            pvAnnuel.setDecision(noteService.decision(moyAnnuel));
            pvAnnuel.setMgp(noteService.mgpPourMoyenneSurVingt(moyAnnuel));
            for (Cours cours : coursList){
                if (cours.getSemestre().getValeur().equals(1)){
                    MoyenneCours moyenneCours = new MoyenneCours();
                    moyenneCours.setMoy(noteService.calculMoyenneCours(etudiant.getId(), cours.getCode(), anneeAcademique.getNumeroDebut()));
                    moyenneCours.setCode(cours.getCode());
                    coursSem1.add(moyenneCours);
                }else {
                    MoyenneCours moyenneCours = new MoyenneCours();
                    moyenneCours.setMoy(noteService.calculMoyenneCours(etudiant.getId(), cours.getCode(), anneeAcademique.getNumeroDebut()));
                    moyenneCours.setCode(cours.getCode());
                    coursSem2.add(moyenneCours);
                }
            }
            pvAnnuelList.add(pvAnnuel);
        }
        return pvAnnuelList;
    }

    public Boolean haveAmodule(String code){
        Cours cours = coursService.getByCode(code);
        if (cours == null){
            return false;
        }
        for (Module module : moduleRepo.findAll()){
            if (module.getCours().getCode().equals(cours.getCode())){
                return true;
            }
        }
        return false;
    }
}