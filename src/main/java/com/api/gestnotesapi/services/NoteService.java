package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.entities.Module;
import com.api.gestnotesapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.api.gestnotesapi.entities.TYPECOURSENUM.*;

@Service
public class NoteService {

    private AnneeAcademiqueRepo anneeAcademiqueRepo;
    private NoteRepo noteRepo;
    private CoursService coursService;
    private AnneeAcademiqueService anneeAcademiqueService;
    private EvaluationService evaluationService;
    private ModuleService moduleService;
    private CreditService creditService;
    private EtudiantService etudiantService;
    private ParcoursService parcoursService;
    private InscriptionService inscriptionService;
    private AnonymatService anonymatService;
    private TypeCoursService typeCoursService;
    private SemestreService semestreService;
    private NiveauService niveauService;
    private CycleService cycleService;
    private DepartementService departementService;
    private OptionService optionService;

    @Autowired
    public NoteService(AnneeAcademiqueRepo anneeAcademiqueRepo, NoteRepo noteRepo, CoursService coursService, AnneeAcademiqueService anneeAcademiqueService, EvaluationService evaluationService, ModuleService moduleService, CreditService creditService, EtudiantService etudiantService, ParcoursService parcoursService, InscriptionService inscriptionService, AnonymatService anonymatService, TypeCoursService typeCoursService, SemestreService semestreService, NiveauService niveauService, CycleService cycleService, DepartementService departementService, OptionService optionService) {
        this.anneeAcademiqueRepo = anneeAcademiqueRepo;
        this.noteRepo = noteRepo;
        this.coursService = coursService;
        this.anneeAcademiqueService = anneeAcademiqueService;
        this.evaluationService = evaluationService;
        this.moduleService = moduleService;
        this.creditService = creditService;
        this.etudiantService = etudiantService;
        this.parcoursService = parcoursService;
        this.inscriptionService = inscriptionService;
        this.anonymatService = anonymatService;
        this.typeCoursService = typeCoursService;
        this.semestreService = semestreService;
        this.niveauService = niveauService;
        this.cycleService = cycleService;
        this.departementService = departementService;
        this.optionService = optionService;
    }

    public List<Note> getNotesEtudiantByCours(
            Long id,
            int session,
            int year,
            String code) {

        Etudiant etudiant = etudiantService.getById(id);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Cours cours = coursService.getByCode(code);

        if (etudiant == null || anneeAcademique == null || cours == null) {
            return null;
        }

        List<Note> noteList = noteRepo.findByCoursAndEtudiantAndAnneeAcademiqueAndIsFinal(cours, etudiant, anneeAcademique, true);
        List<Note> filteredNotes = filterNotesCours(noteList, etudiant, anneeAcademique, cours, session);

        return filteredNotes;
    }

    private List<Note> filterNotesCours(List<Note> noteList, Etudiant etudiant, AnneeAcademique anneeAcademique, Cours cours, int session) {
        return noteList.stream()
                .filter(note ->
                        (etudiant.getMatricule().equals(note.getEtudiant().getMatricule()) &&
                                anneeAcademique.getNumeroDebut().equals(note.getAnneeAcademique().getNumeroDebut()) &&
                                cours.getCode().equals(note.getCours().getCode()) &&
                                note.getIsFinal() &&
                                ((note.getEvaluation().getCode().equals(CodeEva.CC) || note.getEvaluation().getCode().equals(CodeEva.TPE) || note.getEvaluation().getCode().equals(CodeEva.TP)) ||
                                        (note.getEvaluation().getCode().equals(CodeEva.EE) && note.getSessions().equals(session) && note.getIsFinal()))
                        )
                )
                .collect(Collectors.toList());
    }

    private List<Note> filterNotesModule(List<Note> noteList, Etudiant etudiant, AnneeAcademique anneeAcademique, Module module) {
        return noteList.stream()
                .filter(note ->
                        (etudiant.getMatricule().equals(note.getEtudiant().getMatricule()) &&
                                anneeAcademique.getNumeroDebut().equals(note.getAnneeAcademique().getNumeroDebut()) &&
                                module.getCode().equals(note.getModule().getCode()) &&
                                !note.getIsFinal() &&
                                ((note.getEvaluation().getCode().equals(CodeEva.CC) || note.getEvaluation().getCode().equals(CodeEva.TPE) || note.getEvaluation().getCode().equals(CodeEva.TP)) ||
                                        (note.getEvaluation().getCode().equals(CodeEva.EE) && !note.getIsFinal()))
                        )
                )
                .collect(Collectors.toList());
    }

    public Double ccCoursSurTrente(Long id, int year, String code) {

        Etudiant etudiant = etudiantService.getById(id);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Cours cours = coursService.getByCode(code);

        if (etudiant == null || anneeAcademique == null || cours == null) {
            return null;
        }

        List<Note> noteList = noteRepo.findAllByCoursAndAnneeAcademiqueAndIsFinal(cours, anneeAcademique, true);

        Double sommeCC = 0.0, sommeTPE = 0.0, sommeTP = 0.0, ccSurTrente = -1.0;
        TypeCours typeCours = typeCoursService.getById(cours.getTypecours().getId());
        TYPECOURSENUM type = typeCours.getNom();

        for (Note note : noteList) {
            CodeEva evalCode = note.getEvaluation().getCode();
            Etudiant etud = etudiantService.getById(note.getEtudiant().getId());
            if (evalCode.equals(CodeEva.CC) && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                sommeCC = (note.getValeur() != null) ? note.getValeur() : -1.0;
            } else if (evalCode.equals(CodeEva.TPE) && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                sommeTPE = (note.getValeur() != null) ? note.getValeur() : -1.0;
            } else if (evalCode.equals(CodeEva.TP) && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                sommeTP = (note.getValeur() != null) ? note.getValeur() : -1.0;
            }
        }

        // Logique pour calculer la moyenne en fonction du type de cours
        if (type.equals(CC_EE)) {
            ccSurTrente = (sommeCC == -1.0) ? -1.0 : convertirSurTrente(sommeCC);
        } else if (type.equals(CC_TPE_EE)) {
            ccSurTrente = (sommeCC == -1.0 && sommeTPE == -1.0) ? -1.0 :
                    (sommeCC != -1.0 && sommeTPE != -1.0) ? (convertirSurTrente((sommeCC + sommeTPE) / 2)) : (convertirSurTrente(sommeCC + sommeTPE + 1.0));
        } else {
            ccSurTrente = (sommeCC == -1.0 && sommeTPE == -1.0 && sommeTP == -1.0) ? -1.0 :
                    (sommeCC == -1.0 && sommeTPE != -1.0 && sommeTP != -1.0) ? (convertirSurTrente((sommeTPE + sommeTP) / 2)) :
                            (sommeCC != -1.0 && sommeTPE != -1.0 && sommeTP != -1.0) ? (convertirSurTrente( (sommeCC + sommeTPE + sommeTP) / 3)) :
                                    (sommeCC != -1.0 && sommeTPE != -1.0 && sommeTP == -1.0) ? (convertirSurTrente((sommeTPE + sommeCC) / 2)) :
                                            (sommeCC != -1.0 && sommeTPE == -1.0 && sommeTP != -1.0) ? (convertirSurTrente((sommeCC + sommeTP) / 2)) :
                                                    (sommeCC == -1.0 && sommeTPE == -1.0 && sommeTP != -1.0) ? convertirSurTrente(sommeTP) :
                                                            (sommeCC == -1.0 && sommeTPE != -1.0 && sommeTP == -1.0) ? convertirSurTrente(sommeTPE) :
                                                                    (sommeCC != -1.0 && sommeTPE == -1.0 && sommeTP == -1.0) ? convertirSurTrente(sommeCC) : -1.0;
        }
        if (ccSurTrente == -1.0){
            return -1.0;
        }
        Double result = Math.round(ccSurTrente * 100.0) / 100.0;
        return result;
    }

    public Double ccModuleSurTrente(Long id, int year, String code) {

        Etudiant etudiant = etudiantService.getById(id);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Module module = moduleService.getCode(code);

        if (etudiant == null || anneeAcademique == null || module == null) {
            return null;
        }

        List<Note> noteList = noteRepo.findAllByModuleAndAnneeAcademiqueAndIsFinal(module, anneeAcademique, false);

        Double sommeCC = 0.0, sommeTPE = 0.0, sommeTP = 0.0, ccSurTrente = -1.0;
        TypeCours typeCours = typeCoursService.getById(module.getCours().getTypecours().getId());
        TYPECOURSENUM type = typeCours.getNom();

        for (Note note : noteList) {
            CodeEva evalCode = note.getEvaluation().getCode();
            Etudiant etud = etudiantService.getById(note.getEtudiant().getId());
            if (evalCode.equals(CodeEva.CC) && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                sommeCC = (note.getValeur() != null) ? note.getValeur() : -1.0;
            } else if (evalCode.equals(CodeEva.TPE) && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                sommeTPE = (note.getValeur() != null) ? note.getValeur() : -1.0;
            } else if (evalCode.equals(CodeEva.TP) && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                sommeTP = (note.getValeur() != null) ? note.getValeur() : -1.0;
            }
        }

        // Logique pour calculer la moyenne en fonction du type de cours
        if (type.equals(CC_EE)) {
            ccSurTrente = (sommeCC == -1.0) ? -1.0 : convertirSurTrente(sommeCC);
        } else if (type.equals(CC_TPE_EE)) {
            ccSurTrente = (sommeCC == -1.0 && sommeTPE == -1.0) ? -1.0 :
                    (sommeCC != -1.0 && sommeTPE != -1.0) ? (convertirSurTrente((sommeCC + sommeTPE) / 2)) : (convertirSurTrente(sommeCC + sommeTPE + 1.0));
        } else {
            ccSurTrente = (sommeCC == -1.0 && sommeTPE == -1.0 && sommeTP == -1.0) ? -1.0 :
                    (sommeCC == -1.0 && sommeTPE != -1.0 && sommeTP != -1.0) ? (convertirSurTrente((sommeTPE + sommeTP) / 2)) :
                            (sommeCC != -1.0 && sommeTPE != -1.0 && sommeTP != -1.0) ? (convertirSurTrente( (sommeCC + sommeTPE + sommeTP) / 3)) :
                                    (sommeCC != -1.0 && sommeTPE != -1.0 && sommeTP == -1.0) ? (convertirSurTrente((sommeTPE + sommeCC) / 2)) :
                                            (sommeCC != -1.0 && sommeTPE == -1.0 && sommeTP != -1.0) ? (convertirSurTrente((sommeCC + sommeTP) / 2)) :
                                                    (sommeCC == -1.0 && sommeTPE == -1.0 && sommeTP != -1.0) ? convertirSurTrente(sommeTP) :
                                                            (sommeCC == -1.0 && sommeTPE != -1.0 && sommeTP == -1.0) ? convertirSurTrente(sommeTPE) :
                                                                    (sommeCC != -1.0 && sommeTPE == -1.0 && sommeTP == -1.0) ? convertirSurTrente(sommeCC) : -1.0;
        }
        if (ccSurTrente == -1.0){
            return -1.0;
        }
        Double result = Math.round(ccSurTrente * 100.0) / 100.0;
        return result;
    }

    public Double moyenneCoursSurCent(Long id, int session, int year, String code){

        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Cours cours = coursService.getByCode(code);
        Etudiant etudiant = etudiantService.getById(id);
        Double result = 0.0;

        if (etudiant == null || anneeAcademique == null || cours == null) {
            return result;
        }
        List<Note> noteList = noteRepo.findAllByCoursAndAnneeAcademiqueAndIsFinal(cours, anneeAcademique, true);

        Double noteEE = 0.0;
        Double ccSurTrente = ccCoursSurTrente(etudiant.getId(), anneeAcademique.getNumeroDebut(), cours.getCode());

        for (Note note : noteList) {
            CodeEva evalCode = note.getEvaluation().getCode();
            Etudiant etud = etudiantService.getById(note.getEtudiant().getId());
            if (evalCode.equals(CodeEva.EE) && (etud.getMatricule().equals(etudiant.getMatricule())) && (note.getSessions().equals(session))){
                noteEE = (note.getValeur() != null) ? note.getValeur() : -1.0;
            }
        }

        if (ccSurTrente == -1.0 && noteEE == -1.0){
            result = -1.0;
        }else if ((ccSurTrente != -1.0) && (noteEE != -1.0)){
            result = ccSurTrente + noteEE;
        }else if (((ccSurTrente != -1.0) && (noteEE == -1.0)) || ((ccSurTrente == -1.0) && (noteEE != -1.0))){
            result = ccSurTrente + noteEE + 1.0;
        }

        return Math.round(result*100.0)/100.0;
    }


//    A revoir
    public String caculMoyennePondere(int year, String code){

        Cours cours = coursService.getByCode(code);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        if (cours == null){
            return "Le cours specifie n'existe pas";
        }
        List<Note> noteList = noteRepo.findAllByCoursAndIsFinalAndAnneeAcademique(cours, false, anneeAcademique);
        Departement departement = departementService.getByCode(cours.getDepartement().getCode());
        if (departement == null){
            return "Ce cours n'est pas associe a un departement";
        }
        if (noteList == null) {
            return "Aucune note trouvée pour le cours spécifié";
        }
        List<Etudiant> etudiantList = etudiantService.getEtudiantByDepartement(departement.getCode());

        Credit credit = creditService.getById(cours.getCredit().getId());
        int creditCours = credit.getValeur();

        TypeCours typeCours = typeCoursService.getById(cours.getTypecours().getId());
        TYPECOURSENUM type = typeCours.getNom();
        for (Etudiant etudiant : etudiantList){
            Double sommeCC = -1.0, sommeTPE = -1.0, sommeTP = -1.0;
            for (Note note : noteList){
                Evaluation evaluation = evaluationService.getById(note.getEvaluation().getId());
                Module module = moduleService.getById(note.getModule().getId());
                Credit creditModule = creditService.getById(module.getCredit().getId());
                Etudiant etud = etudiantService.getById(note.getEtudiant().getId());

                if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals(CodeEva.CC))){
                    if (note.getValeur() != null) {
                        if (sommeCC == -1.0){
                            sommeCC = note.getValeur() * creditModule.getValeur();
                        }else{
                            sommeCC += note.getValeur() * creditModule.getValeur();
                        }
                    }
                }else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals(CodeEva.TPE))){
                    if (note.getValeur() != null) {
                        if (sommeTPE == -1.0) {
                            sommeTPE = note.getValeur() * creditModule.getValeur();
                        } else {
                            sommeTPE += note.getValeur() * creditModule.getValeur();
                        }
                    }
                }else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals(CodeEva.TP))){
                    if (note.getValeur() != null) {
                        if (sommeTP == -1.0) {
                            sommeTP = note.getValeur() * creditModule.getValeur();
                        } else {
                            sommeTP += note.getValeur() * creditModule.getValeur();
                        }
                    }
                }

            }
            Double valeurCC = 0.0, valeurTPE = 0.0,valeurTP = 0.0;
            if (type.equals(CC_TPE_TP_EE)){
                Note newNote1 = new Note();
                if (sommeCC == -1.0){
                    newNote1.setSessions(0);
                    newNote1.setCours(cours);
                    newNote1.setEtudiant(etudiant);
                    newNote1.setEvaluation(evaluationService.getByCode(CodeEva.CC));
                    newNote1.setIsFinal(true);
                    newNote1.setAnneeAcademique(anneeAcademique);

                    noteRepo.save(newNote1);
                }else {
                    valeurCC = sommeCC/creditCours;
                    Double resultCC = Math.round(valeurCC*100.0)/100.0;

                    newNote1.setValeur(resultCC);
                    newNote1.setSessions(0);
                    newNote1.setCours(cours);
                    newNote1.setEtudiant(etudiant);
                    newNote1.setEvaluation(evaluationService.getByCode(CodeEva.CC));
                    newNote1.setIsFinal(true);
                    newNote1.setAnneeAcademique(anneeAcademique);

                    ajouterNoteCours(newNote1);
                }

                Note newNote2 = new Note();
                if (sommeTPE == -1.0){
                    newNote2.setSessions(0);
                    newNote2.setCours(cours);
                    newNote2.setEtudiant(etudiant);
                    newNote2.setEvaluation(evaluationService.getByCode(CodeEva.TPE));
                    newNote2.setIsFinal(true);
                    newNote2.setAnneeAcademique(noteList.get(0).getAnneeAcademique());

                    ajouterNoteCours(newNote2);
                }else {
                    valeurTPE = sommeTPE/creditCours;
                    Double resultTPE = Math.round(valeurTPE*100.0)/100.0;
                    newNote2.setValeur(resultTPE);
                    newNote2.setSessions(0);
                    newNote2.setCours(cours);
                    newNote2.setEtudiant(etudiant);
                    newNote2.setEvaluation(evaluationService.getByCode(CodeEva.TPE));
                    newNote2.setIsFinal(true);
                    newNote2.setAnneeAcademique(noteList.get(0).getAnneeAcademique());

                    ajouterNoteCours(newNote2);
                }

                Note newNote3 = new Note();
                if (sommeTP == -1.0){
                    newNote3.setSessions(0);
                    newNote3.setCours(cours);
                    newNote3.setEtudiant(etudiant);
                    newNote3.setEvaluation(evaluationService.getByCode(CodeEva.TP));
                    newNote3.setIsFinal(true);
                    newNote3.setAnneeAcademique(noteList.get(0).getAnneeAcademique());

                    ajouterNoteCours(newNote3);
                }else {
                    valeurTP = sommeTP/creditCours;
                    Double resultTP = Math.round(valeurTP*100.0)/100.0;
                    newNote3.setValeur(resultTP);
                    newNote3.setSessions(0);
                    newNote3.setCours(cours);
                    newNote3.setEtudiant(etudiant);
                    newNote3.setEvaluation(evaluationService.getByCode(CodeEva.TP));
                    newNote3.setIsFinal(true);
                    newNote3.setAnneeAcademique(noteList.get(0).getAnneeAcademique());

                    ajouterNoteCours(newNote3);
                }


            }else if (type.equals(CC_TPE_EE)){
                Note newNote1 = new Note();
                if (sommeCC == -1.0){
                    newNote1.setSessions(0);
                    newNote1.setCours(cours);
                    newNote1.setEtudiant(etudiant);
                    newNote1.setEvaluation(evaluationService.getByCode(CodeEva.CC));
                    newNote1.setIsFinal(true);
                    newNote1.setAnneeAcademique(anneeAcademique);

                    ajouterNoteCours(newNote1);
                }else {
                    valeurCC = sommeCC/creditCours;
                    double resultCC = Math.round(valeurCC*100.0)/100.0;

                    newNote1.setValeur(resultCC);
                    newNote1.setSessions(0);
                    newNote1.setCours(cours);
                    newNote1.setEtudiant(etudiant);
                    newNote1.setEvaluation(evaluationService.getByCode(CodeEva.CC));
                    newNote1.setIsFinal(true);
                    newNote1.setAnneeAcademique(anneeAcademique);

                    ajouterNoteCours(newNote1);
                }

                Note newNote2 = new Note();
                if (sommeTPE == -1.0){
                    newNote2.setSessions(0);
                    newNote2.setCours(cours);
                    newNote2.setEtudiant(etudiant);
                    newNote2.setEvaluation(evaluationService.getByCode(CodeEva.TPE));
                    newNote2.setIsFinal(true);
                    newNote2.setAnneeAcademique(noteList.get(0).getAnneeAcademique());

                    ajouterNoteCours(newNote2);
                }else {
                    valeurTPE = sommeTPE/creditCours;
                    double resultTPE = Math.round(valeurTPE*100.0)/100.0;
                    newNote2.setValeur(resultTPE);
                    newNote2.setSessions(0);
                    newNote2.setCours(cours);
                    newNote2.setEtudiant(etudiant);
                    newNote2.setEvaluation(evaluationService.getByCode(CodeEva.TPE));
                    newNote2.setIsFinal(true);
                    newNote2.setAnneeAcademique(noteList.get(0).getAnneeAcademique());

                    ajouterNoteCours(newNote2);
                }

            }else if (type.equals(CC_EE)){
                Note newNote1 = new Note();
                if (sommeCC == -1.0){
                    newNote1.setSessions(0);
                    newNote1.setCours(cours);
                    newNote1.setEtudiant(etudiant);
                    newNote1.setEvaluation(evaluationService.getByCode(CodeEva.CC));
                    newNote1.setIsFinal(true);
                    newNote1.setAnneeAcademique(anneeAcademique);

                    ajouterNoteCours(newNote1);
                }else {
                    valeurCC = sommeCC/creditCours;
                    double resultCC = Math.round(valeurCC*100.0)/100.0;

                    newNote1.setValeur(resultCC);
                    newNote1.setSessions(0);
                    newNote1.setCours(cours);
                    newNote1.setEtudiant(etudiant);
                    newNote1.setEvaluation(evaluationService.getByCode(CodeEva.CC));
                    newNote1.setIsFinal(true);
                    newNote1.setAnneeAcademique(anneeAcademique);

                    ajouterNoteCours(newNote1);
                }
            }
        }

        return "Opération effectuée avec succès";
    }

    public Note ajouterNoteCours(Note note){

        if (note == null) {
            return null;
        }

        note.setIsFinal(true);

        Cours cours = coursService.getById(note.getCours().getCoursId());
        TypeCours typeCours = typeCoursService.getById(cours.getTypecours().getId());
        Evaluation evaluation = evaluationService.getById(note.getEvaluation().getId());

        if (typeCours == null || evaluation == null) {
            return null;
        }

        TYPECOURSENUM type = typeCours.getNom();
        boolean isValidType = (type.equals(CC_TPE_EE) && evaluation.getCode().equals(CodeEva.TP)) ||
                (type.equals(CC_EE) && (evaluation.getCode().equals(CodeEva.TP) || evaluation.getCode().equals(CodeEva.TPE)));

        if (isValidType) {
            return null;
        }
//        if (note.getValeur() == null){
//            note.setValeur(null);
//        }
        return noteRepo.save(note);
    }

    public Note ajouterNoteModule(Note note){

        if (note == null) {
            return null;
        }
        Cours cours = coursService.getById(note.getModule().getCours().getCoursId());
        TypeCours typeCours = typeCoursService.getById(cours.getTypecours().getId());
        Evaluation evaluation = evaluationService.getById(note.getEvaluation().getId());

        if (typeCours == null || evaluation == null) {
            return null;
        }

        TYPECOURSENUM type = typeCours.getNom();
        boolean isValidType = (type.equals(CC_TPE_EE) && evaluation.getCode().equals(CodeEva.TP)) ||
                (type.equals(CC_EE) && (evaluation.getCode().equals(CodeEva.TP) || evaluation.getCode().equals(CodeEva.TPE)));

        if (isValidType) {
            return null;
        }
//        if (note.getValeur() == null){
//            note.setValeur(null);
//        }

        return noteRepo.save(note);
    }

    public Note ajouterNoteExamen(String valeur, Double noteUE) {

        Anonymat anonymat = anonymatService.getByValeur(valeur);
        AnneeAcademique anneeAcademique = anneeAcademiqueService.getById(anonymat.getAnneeAcademique().getId());
        Etudiant etudiant = etudiantService.getById(anonymat.getEtudiant().getId());
        Cours cours = coursService.getById(anonymat.getCours().getCoursId());
        Module module = moduleService.getById(cours.getCoursId());
        Evaluation evaluation = evaluationService.getByCode(CodeEva.EE);

        if (anneeAcademique == null || etudiant == null || cours == null || evaluation == null || anonymat == null) {
            return null;
        }
            Note note = new Note();

            note.setEvaluation(evaluation);
            note.setModule(module);
            note.setEtudiant(etudiant);
            note.setCours(cours);
            note.setSessions(anonymat.getSessions());
            note.setValeur(noteUE);
//            if (noteUE != -1.0){
//                note.setValeur(noteUE);
//            }
            note.setAnneeAcademique(anneeAcademique);
            note.setIsFinal(true);

            return noteRepo.save(note);
    }

    public Double moyenneSemestre(Long id, int annee, int semestre) {
        Etudiant etudiant = etudiantService.getById(id);

        if (etudiant == null) {
            return null;
        }

        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(annee);

        Semestre semestreEntity = semestreService.getValeur(semestre);

        if (semestreEntity == null) {
            return null;
        }

        Double sum = 0.0;
        int creditTotal = 0;
        Parcours parcours = parcoursService.getParcoursEtudiant(etudiant.getId(), anneeAcademique.getCode());
        List<Cours> coursList = coursService.getListCoursByParcours(parcours.getLabel());
        for (Cours cours : coursList) {
            Double moyenneCours = calculMoyenneCours(etudiant.getId(), cours.getCode(), anneeAcademique.getNumeroDebut());
            if (moyenneCours != -1.0) {
                sum += moyenneCours * cours.getCredit().getValeur();
                creditTotal += cours.getCredit().getValeur();
            }
        }

        if (creditTotal == 0) {
            return 0.0;
        }

        Double moyenneSemestre = sum / creditTotal;
        return moyenneSemestre;
    }

    public Double calculMoyenneCours(Long id, String code, int annee) {
        Etudiant etudiant = etudiantService.getById(id);
        Cours cours = coursService.getByCode(code);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(annee);
        if (etudiant == null || cours == null || anneeAcademique == null){
            return null;
        }
        Double result = null, noteExamen = null, noteCC = null, noteTPE = null, noteTP = null;
        List<Note> noteEE = new ArrayList<>();
        List<Note> noteList = noteRepo.findByCoursAndEtudiantAndAnneeAcademiqueAndIsFinal(cours, etudiant, anneeAcademique, true);
        for (Note note : noteList) {
            Evaluation evaluation = note.getEvaluation();
            if (evaluation.getCode() == CodeEva.EE) {
                if (note.getValeur() != null){
                    noteEE.add(note);
                }
            } else if (evaluation.getCode() == CodeEva.CC) {
                if (note.getValeur() != null){
                    noteCC = note.getValeur();
                }
            } else if (evaluation.getCode() == CodeEva.TPE) {
                if (note.getValeur() != null){
                    noteTPE = note.getValeur();
                }
            } else if (evaluation.getCode() == CodeEva.TP) {
                if (note.getValeur() != null){
                    noteTP = note.getValeur();
                }
            }

        }

        TypeCours typeCours = typeCoursService.getById(cours.getTypecours().getId());

        if (typeCours == null) {
            return null;
        }
        if (noteEE == null){
            return null;
        }else {
            if (noteEE.size() == 2){
                for (Note note : noteEE){
                    if (note.getSessions() == 2 && note.getValeur() != null){
                        noteExamen = note.getValeur();
                    }else if (note.getSessions() == 2 && note.getValeur() == null){
                        noteExamen = null;
                    }
                }
            }else{
                for (Note note : noteEE){
                    if (note.getValeur() != null){
                        noteExamen = note.getValeur();
                    }else {
                        noteExamen = null;
                    }
                }
            }

        }
        if (typeCours.getNom().equals(CC_TPE_TP_EE)) {
            if (noteExamen == null) {
                result = null;
            }else {
                if (noteCC == null && noteTPE == null && noteTP == null){
                    result = null;
                }else {
                    result = 0.7 * noteExamen
                            + (0.1 * noteCC > 0 ? 0.1 * noteCC : 0)
                            + (0.1 * noteTPE > 0 ? 0.1 * noteTPE : 0)
                            + (0.1 * noteTP > 0 ? 0.1 * noteTP : 0);
                }
            }
        } else if (typeCours.getNom().equals(CC_TPE_EE)) {
            if (noteExamen == null) {
                result = null;
            }else {
                if (noteCC == null && noteTPE == null){
                    result = null;
                }else{
                    result = 0.7 * noteExamen
                            + (0.2 * noteCC > 0 ? 0.2 * noteCC : 0)
                            + (0.1 * noteTPE > 0 ? 0.1 * noteTPE : 0);
                }
            }
        } else if (typeCours.getNom().equals(CC_EE)) {
            if (noteExamen == null || noteCC == null) {
                result = null;
            }else {
                Double temp = examenSurVingt(noteExamen);
                result = 0.7 * temp + 0.3 * noteCC;
            }
        }
        return Math.round(result*100.0)/100.0;
    }

    public Integer nombreCoursEtudiant(Etudiant etudiant, AnneeAcademique anneeAcademique){
        return coursService.getListCoursByParcours(parcoursService.getParcoursEtudiant(etudiant.getId(), anneeAcademique.getCode()).getLabel()).size();
    }

    public Double examenSurVingt(Double note){
        return (note * 20)/70;
    }

    public Double moyenneAnnuelle(Long id, int annee){
        Double semestre1 = moyenneSemestre(id, annee, 1);
        Double semestre2 = moyenneSemestre(id, annee, 2);
        Double result = -1.0;
        if (semestre1 == -1.0 && semestre2 == -1.0){
            return result;
        }else if (semestre1 != -1.0 && semestre2 != -1.0){
            result = (semestre1 + semestre2)/2;
        }else if ((semestre1 != -1.0 && semestre2 == -1.0) || (semestre1 == -1.0 && semestre2 != -1.0)){
            result = semestre1 + semestre2 + 1.0;
        }
        return result;
    }

    public int creditSemestre(Long id, int annee, int semestre, String code){

        int creditTotal = 0;
        Etudiant etudiant = etudiantService.getById(id);

        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(annee);
        Parcours parcours = parcoursService.getParcoursEtudiant(id, anneeAcademique.getCode());

        Semestre semestreEntity = semestreService.getValeur(semestre);

        if (etudiant == null || anneeAcademique == null || parcours == null || semestreEntity == null) {
            return 0;
        }
        for (Cours cours : coursService.getListCoursByParcours(parcours.getLabel())) {
            Double moyenneCours = calculMoyenneCours(etudiant.getId(), cours.getCode(), anneeAcademique.getNumeroDebut());
            if ((moyenneCours != -1.0) && (moyenneCours >= 10.0)) {
                creditTotal += cours.getCredit().getValeur();
            }
        }
        return creditTotal;
    }

    public int creditAnnuelle(Long id, int annee, String code){

        int creditSemestre1 = creditSemestre(id, annee, 1, code);
        int creditSemestre2 = creditSemestre(id, annee, 2, code);

        return creditSemestre1 + creditSemestre2;
    }

    public List<Etudiant> getListPassageByParcours(String label, int year){

        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Parcours parcours = parcoursService.getByLabel(label);
        Niveau niveau = niveauService.getById(parcours.getNiveau().getId());
        Option option = optionService.getById(parcours.getOption().getId());
        List<Etudiant> passageList = new ArrayList<>();
        if (anneeAcademique == null || parcours == null || niveau == null || option == null || option == null){
            return null;
        }
        Departement departement = departementService.getById(option.getDepartement().getId());
        List<Etudiant> etudiantList = etudiantService.getListEtudiantByParcours(parcours.getLabel(), anneeAcademique.getNumeroDebut());
        if (etudiantList == null || departement == null){
            return null;
        }

        String code = departement.getCode();
        for (Etudiant etudiant : etudiantList){
            int creditSem1 = creditSemestre(etudiant.getId(), year, 1, code), creditSem2 = creditSemestre(etudiant.getId(), year, 2, code);
            int creditTotal = creditAnnuelle(etudiant.getId(), year, code);

//            En attente
            int min = creditSem1 - 6;
            if ((niveau.getValeur().equals(2)) || (niveau.getValeur().equals(4))){
                if ((creditSem1 >= min) && (creditSem2 == 6) && (etudiant.getValideAll().equals(true)) && (etudiant.getValide().equals(true))){
                    passageList.add(etudiant);

                    if (creditTotal < 12){
                        etudiant.setValide(true);
                        etudiant.setValideAll(false);
                        etudiantService.addEtudiant(etudiant);
                    }else if (creditTotal == 12){
                        etudiant.setValide(true);
                        etudiant.setValideAll(true);
                        etudiantService.addEtudiant(etudiant);
                    }
                }
            }else if ((creditTotal >= 9) && (niveau.getValeur().equals(1))){
                passageList.add(etudiant);

                if (creditTotal < 12){
                    etudiant.setValide(true);
                    etudiant.setValideAll(false);
                    etudiantService.addEtudiant(etudiant);
                }else if (creditTotal == 12){
                    etudiant.setValide(true);
                    etudiant.setValideAll(true);
                    etudiantService.addEtudiant(etudiant);
                }
            }else {
                if (creditTotal < 12){
                    etudiant.setValide(false);
                    etudiant.setValideAll(false);
                    etudiantService.addEtudiant(etudiant);
                }else if (creditTotal == 12){
                    etudiant.setValide(true);
                    etudiant.setValideAll(true);
                    Etudiant etud = etudiantService.addEtudiant(etudiant);
                    passageList.add(etud);
                }
            }
        }

        return passageList;
    }

    public Double convertirSurTrente(Double note){
        return (30*note)/20;
    }
    public Double convertirSurVingt(Double note){
        return Math.round(((20*note)/100)*100.0)/100.0;
    }
    public String decision(Double note){
        String decision = "";
        if (note == -1.0){
            decision = "EL";
        }else if(note >= 10.0){
            decision = "CA";
        }else if((note >= 8.0) && (note <= 9.99)){
            decision = "CANT";
        }else if(note <= 7.99){
            decision = "NCA";
        }
        return decision;
    }
    public Double mgpPourMoyenneSurVingt(Double note){
        Double valeur = 0.0;
        if (note == -1.0){
            valeur = -1.0;
        }else if ((note >= 0.0) && (note <= 6.0)){
            valeur = 0.0;
        }else if ((note >= 6.0) && (note <= 8.0)){
            valeur = 1.0;
        }else if ((note >= 8.0) && (note <= 9.0)){
            valeur = 1.3;
        }else if ((note >= 9.0) && (note <= 10.0)){
            valeur = 1.7;
        }else if ((note >= 10.0) && (note <= 11.0)){
            valeur = 2.0;
        }else if ((note >= 11.0) && (note <= 12.0)){
            valeur = 2.3;
        }else if ((note >= 12.0) && (note <= 13.0)){
            valeur = 2.7;
        }else if ((note >= 13.0) && (note <= 14.0)){
            valeur = 3.0;
        }else if ((note >= 14.0) && (note <= 16.0)){
            valeur = 3.3;
        }else if ((note >= 16.0) && (note <= 18.0)){
            valeur = 3.7;
        }else if ((note >= 18.0) && (note <= 20.0)){
            valeur = 4.0;
        }

        return valeur;
    }

    public Double mgpPourMoyenneSurCent(Double note){
        Double valeur = 0.0;
        if (note == -1.0){
            valeur = -1.0;
        }else if ((note >= 0.0) && (note <= 30.0)){
            valeur = 0.0;
        }else if ((note >= 30.0) && (note <= 40.0)){
            valeur = 1.0;
        }else if ((note >= 40.0) && (note <= 45.0)){
            valeur = 1.3;
        }else if ((note >= 45.0) && (note <= 50.0)){
            valeur = 1.7;
        }else if ((note >= 50.0) && (note <= 55.0)){
            valeur = 2.0;
        }else if ((note >= 55.0) && (note <= 60.0)){
            valeur = 2.3;
        }else if ((note >= 60.0) && (note <= 65.0)){
            valeur = 2.7;
        }else if ((note >= 65.0) && (note <= 70.0)){
            valeur = 3.0;
        }else if ((note >= 70.0) && (note <= 80.0)){
            valeur = 3.3;
        }else if ((note >= 80.0) && (note <= 90.0)){
            valeur = 3.7;
        }else if ((note >= 90.0) && (note <= 100.0)){
            valeur = 4.0;
        }

        return valeur;
    }

    public String grade(Double mgp){
        String valeur = "";
        if (mgp == -1.0){
            valeur = null;
        }else if (mgp == 0.0){
            valeur = "F";
        }else if (mgp == 1.0){
            valeur = "E";
        }else if (mgp == 1.3){
            valeur = "D";
        }else if (mgp == 1.7){
            valeur = "C-";
        }else if (mgp == 2.0){
            valeur = "C";
        }else if (mgp == 2.3){
            valeur = "C+";
        }else if (mgp == 2.7){
            valeur = "B-";
        }else if (mgp == 3){
            valeur = "B";
        }else if (mgp == 3.3){
            valeur = "B+";
        }else if (mgp == 3.7){
            valeur = "A";
        }else if (mgp == 4.0){
            valeur = "A+";
        }

        return valeur;
    }

//    public Double getNoteStageFromEtudiant(Long id, List<Note> noteList){
//
//
//    }

//    public List<PVGrandJuryResponse> getPVGrandJury(PVGrandJuryRequest pvGrandJuryRequest){
//
//        Parcours parcours = parcoursRepo.findByLabel(pvGrandJuryRequest.getParcours()).orElse(null);
//        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByCode(pvGrandJuryRequest.getAnneeAca()).orElse(null);
//        if (parcours == null || anneeAcademique == null){
//            return null;
//        }
//
//        Option option = optionRepo.findByCode(parcours.getOption().getCode()).orElse(null);
//        if (option == null){
//            return null;
//        }
//        Departement departement = departementRepo.findByCode(option.getDepartement().getCode()).orElse(null);
//        if (departement == null){
//            return null;
//        }
//
//        List<Cours> coursList = coursService.getListCoursByParcours(parcours.getLabel());
//        int size = coursList.size();
//        List<Etudiant> etudiantList = etudiantService.getListEtudiantByParcours(parcours.getLabel(), anneeAcademique.getNumeroDebut(), pvGrandJuryRequest.getType());
//        if (coursList == null || etudiantList == null){
//            return null;
//        }
//        List<MoyenneCours> moyenneCoursList = new ArrayList<>();
//        for (Etudiant etudiant : etudiantList){
//            PVGrandJuryResponse pvGrandJuryResponse = new PVGrandJuryResponse();
//            Double moyGen = 0.0;
//            pvGrandJuryResponse.setEtudiant(etudiant);
//            for (Cours cours : coursList){
//                Double moy = calculMoyenneCours(etudiant, cours, anneeAcademique);
//                moyGen += moy;
//                MoyenneCours moyenneCours = new MoyenneCours(cours, moy);
//                moyenneCoursList.add(moyenneCours);
//            }
//        }
//    }

//    public Integer getRank(Long id, Listv)

    public List<Note> getNotesEtudiantByModule(Long id, int year, String code) {
        Etudiant etudiant = etudiantService.getById(id);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Module module = moduleService.getCode(code);

        if (etudiant == null || anneeAcademique == null || module == null) {
            return null;
        }

        List<Note> noteList = noteRepo.findAllByEtudiantAndAnneeAcademiqueAndModule(etudiant, anneeAcademique, module);
        List<Note> filteredNotes = filterNotesModule(noteList, etudiant, anneeAcademique, module);

        return filteredNotes;
    }

    public Note update(Long id, Note note) {
        Note no = noteRepo.findById(id).orElse(null);
        if (no == null){
            return null;
        }
        no.setValeur(note.getValeur());
        no.setModule(note.getModule());
        no.setCours(note.getCours());
        no.setAnneeAcademique(note.getAnneeAcademique());
        no.setEtudiant(note.getEtudiant());
        no.setEvaluation(note.getEvaluation());

        return noteRepo.save(no);
    }

    public void delete(Long id) {
        noteRepo.deleteById(id);
    }
}
