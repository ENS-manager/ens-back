package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.entities.Module;
import com.api.gestnotesapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private EtudiantRepo etudiantRepo;
    private AnneeAcademiqueRepo anneeAcademiqueRepo;
    private CoursRepo coursRepo;
    private NoteRepo noteRepo;
    private TypeCoursRepo typeCoursRepo;
    private CreditRepo creditRepo;
    private EvaluationRepo evaluationRepo;
    private ModuleRepo moduleRepo;
    private AnonymatRepo anonymatRepo;
    private SemestreRepo semestreRepo;
    private ParcoursRepo parcoursRepo;
    private NiveauRepo niveauRepo;
    private InscriptionRepo inscriptionRepo;
    private ParcoursService parcoursService;
    private EtudiantService etudiantService;
    private DepartementRepo departementRepo;
    private OptionRepo optionRepo;

    @Autowired
    public NoteService(EtudiantRepo etudiantRepo, AnneeAcademiqueRepo anneeAcademiqueRepo, CoursRepo coursRepo, NoteRepo noteRepo, TypeCoursRepo typeCoursRepo, CreditRepo creditRepo, EvaluationRepo evaluationRepo, ModuleRepo moduleRepo, AnonymatRepo anonymatRepo, SemestreRepo semestreRepo, ParcoursRepo parcoursRepo, NiveauRepo niveauRepo, InscriptionRepo inscriptionRepo, ParcoursService parcoursService, EtudiantService etudiantService, DepartementRepo departementRepo, OptionRepo optionRepo) {
        this.etudiantRepo = etudiantRepo;
        this.anneeAcademiqueRepo = anneeAcademiqueRepo;
        this.coursRepo = coursRepo;
        this.noteRepo = noteRepo;
        this.typeCoursRepo = typeCoursRepo;
        this.creditRepo = creditRepo;
        this.evaluationRepo = evaluationRepo;
        this.moduleRepo = moduleRepo;
        this.anonymatRepo = anonymatRepo;
        this.semestreRepo = semestreRepo;
        this.parcoursRepo = parcoursRepo;
        this.niveauRepo = niveauRepo;
        this.inscriptionRepo = inscriptionRepo;
        this.parcoursService = parcoursService;
        this.etudiantService = etudiantService;
        this.departementRepo = departementRepo;
        this.optionRepo = optionRepo;
    }

    public List<Note> getNotesEtudiantByCours(
            Long id,
            int session,
            int year,
            String code) {

        Etudiant etudiant = etudiantRepo.findById(id).orElse(null);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Cours cours = coursRepo.findByCode(code).orElse(null);

        if (etudiant == null || anneeAcademique == null || cours == null) {
            return null;
        }

        List<Note> noteList = noteRepo.findAllByEtudiantAndAnneeAcademiqueAndCoursAndIsFinal(etudiant, anneeAcademique, cours, true);
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

        Etudiant etudiant = etudiantRepo.findById(id).orElse(null);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Cours cours = coursRepo.findByCode(code).orElse(null);

        if (etudiant == null || anneeAcademique == null || cours == null) {
            return null;
        }

        List<Note> noteList = noteRepo.findAllByCoursAndAnneeAcademiqueAndIsFinal(cours, anneeAcademique, true);

        Double sommeCC = 0.0, sommeTPE = 0.0, sommeTP = 0.0, ccSurTrente = -1.0;
        TypeCours typeCours = typeCoursRepo.findById(cours.getTypecours().getId()).get();
        String type = typeCours.getNom();

        for (Note note : noteList) {
            CodeEva evalCode = note.getEvaluation().getCode();
            Etudiant etud = etudiantRepo.findById(note.getEtudiant().getId()).get();
            if (evalCode.equals(CodeEva.CC) && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                sommeCC = (note.getValeur() != null) ? note.getValeur() : -1.0;
            } else if (evalCode.equals(CodeEva.TPE) && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                sommeTPE = (note.getValeur() != null) ? note.getValeur() : -1.0;
            } else if (evalCode.equals(CodeEva.TP) && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                sommeTP = (note.getValeur() != null) ? note.getValeur() : -1.0;
            }
        }

        // Logique pour calculer la moyenne en fonction du type de cours
        if (type.equals("CC, EE")) {
            ccSurTrente = (sommeCC == -1.0) ? -1.0 : convertirSurTrente(sommeCC);
        } else if (type.equals("CC, TPE, EE")) {
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

        Etudiant etudiant = etudiantRepo.findById(id).orElse(null);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Module module = moduleRepo.findByCode(code).orElse(null);

        if (etudiant == null || anneeAcademique == null || module == null) {
            return null;
        }

        List<Note> noteList = noteRepo.findAllByModuleAndAnneeAcademiqueAndIsFinal(module, anneeAcademique, false);

        Double sommeCC = 0.0, sommeTPE = 0.0, sommeTP = 0.0, ccSurTrente = -1.0;
        TypeCours typeCours = typeCoursRepo.findById(module.getCours().getTypecours().getId()).get();
        String type = typeCours.getNom();

        for (Note note : noteList) {
            CodeEva evalCode = note.getEvaluation().getCode();
            Etudiant etud = etudiantRepo.findById(note.getEtudiant().getId()).get();
            if (evalCode.equals(CodeEva.CC) && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                sommeCC = (note.getValeur() != null) ? note.getValeur() : -1.0;
            } else if (evalCode.equals(CodeEva.TPE) && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                sommeTPE = (note.getValeur() != null) ? note.getValeur() : -1.0;
            } else if (evalCode.equals(CodeEva.TP) && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                sommeTP = (note.getValeur() != null) ? note.getValeur() : -1.0;
            }
        }

        // Logique pour calculer la moyenne en fonction du type de cours
        if (type.equals("CC, EE")) {
            ccSurTrente = (sommeCC == -1.0) ? -1.0 : convertirSurTrente(sommeCC);
        } else if (type.equals("CC, TPE, EE")) {
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
        Cours cours = coursRepo.findByCode(code).orElse(null);
        Etudiant etudiant = etudiantRepo.findById(id).orElse(null);
        Double result = 0.0;

        if (etudiant == null || anneeAcademique == null || cours == null) {
            return result;
        }
        List<Note> noteList = noteRepo.findAllByCoursAndAnneeAcademiqueAndIsFinal(cours, anneeAcademique, true);

        Double noteEE = 0.0;
        Double ccSurTrente = ccCoursSurTrente(etudiant.getId(), anneeAcademique.getNumeroDebut(), cours.getCode());

        for (Note note : noteList) {
            CodeEva evalCode = note.getEvaluation().getCode();
            Etudiant etud = etudiantRepo.findById(note.getEtudiant().getId()).get();
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

        return result;
    }

    public Double moyenneCoursSurVingt(Long id, int session, int year, String code){

        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Cours cours = coursRepo.findByCode(code).orElse(null);
        Etudiant etudiant = etudiantRepo.findById(id).orElse(null);
        Double result = 0.0;

        if (etudiant == null || anneeAcademique == null || cours == null) {
            return result;
        }

        result = moyenneCoursSurVingt(etudiant.getId(), session, anneeAcademique.getNumeroDebut(), cours.getCode());
        if (result == -1.0){
            return result;
        }
        return convertirSurVingt(result);
    }

    public String caculMoyennePondere(int year, String code){

        Optional<Cours> cours = coursRepo.findByCode(code);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        List<Note> noteList = noteRepo.findAllByCoursAndIsFinalAndAnneeAcademique(cours.get(), false, anneeAcademique);

        if (noteList.isEmpty()) {
            return "Aucune note trouvée pour le cours spécifié";
        }

        Credit credit = creditRepo.findById(cours.get().getCredit().getId()).get();
        int creditCours = credit.getValeur();

        TypeCours typeCours = typeCoursRepo.findById(cours.get().getTypecours().getId()).get();
        String type = typeCours.getNom();
        for (Etudiant etudiant : etudiantRepo.findAll()){
            Double sommeCC = 0.0, sommeTPE = 0.0, sommeTP = 0.0;
            int i = 0, j = 0, k = 0;
            for (Note note : noteList){
                Evaluation evaluation = evaluationRepo.findById(note.getEvaluation().getId()).get();
                Module module = moduleRepo.findById(note.getModule().getId()).get();
                Credit creditModule = creditRepo.findById(module.getCredit().getId()).get();
                Etudiant etud = etudiantRepo.findById(note.getEtudiant().getId()).get();

                if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals(CodeEva.CC))){
                    if (note.getValeur() == null){
                        if (i == 0){
                            sommeCC = -1.0;
                        }
                    }else {
                        if (sommeCC == -1.0){
                            sommeCC = (creditModule.getValeur()*note.getValeur());
                        }else {
                            sommeCC += (creditModule.getValeur()*note.getValeur());
                            i++;
                        }
                    }
                }else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals(CodeEva.TPE))){
                    if (note.getValeur() == null){
                        if (j == 0){
                            sommeTPE = -1.0;
                        }
                    }else {
                        if (sommeTPE == -1.0){
                            sommeTPE = (creditModule.getValeur()*note.getValeur());
                        }else {
                            sommeTPE += (creditModule.getValeur()*note.getValeur());
                            j++;
                        }
                    }
                }else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals(CodeEva.TP))){
                    if (note.getValeur() == null){
                        if (k == 0){
                            sommeTP = -1.0;
                        }
                    }else {
                        if (sommeTP == -1.0){
                            sommeTP = (creditModule.getValeur()*note.getValeur());
                        }else {
                            sommeTP += (creditModule.getValeur()*note.getValeur());
                            k++;
                        }
                    }
                }

            }
            Double valeurCC = 0.0, valeurTPE = 0.0,valeurTP = 0.0;
            if (type.equals("CC, TPE, TP, EE")){
                Note newNote1 = new Note();
                if (sommeCC == -1.0){
                    newNote1.setSessions(0);
                    newNote1.setCours(cours.get());
                    newNote1.setEtudiant(etudiant);
                    newNote1.setEvaluation(evaluationRepo.findByCode(CodeEva.CC).get());
                    newNote1.setIsFinal(true);
                    newNote1.setAnneeAcademique(anneeAcademique);

                    noteRepo.save(newNote1);
                }else {
                    valeurCC = sommeCC/creditCours;
                    Double resultCC = Math.round(valeurCC*100.0)/100.0;

                    newNote1.setValeur(resultCC);
                    newNote1.setSessions(0);
                    newNote1.setCours(cours.get());
                    newNote1.setEtudiant(etudiant);
                    newNote1.setEvaluation(evaluationRepo.findByCode(CodeEva.CC).get());
                    newNote1.setIsFinal(true);
                    newNote1.setAnneeAcademique(anneeAcademique);

                    noteRepo.save(newNote1);
                }

                Note newNote2 = new Note();
                if (sommeTPE == -1.0){
                    newNote2.setSessions(0);
                    newNote2.setCours(cours.get());
                    newNote2.setEtudiant(etudiant);
                    newNote2.setEvaluation(evaluationRepo.findByCode(CodeEva.TPE).get());
                    newNote2.setIsFinal(true);
                    newNote2.setAnneeAcademique(noteList.get(0).getAnneeAcademique());

                    noteRepo.save(newNote2);
                }else {
                    valeurTPE = sommeTPE/creditCours;
                    Double resultTPE = Math.round(valeurTPE*100.0)/100.0;
                    newNote2.setValeur(resultTPE);
                    newNote2.setSessions(0);
                    newNote2.setCours(cours.get());
                    newNote2.setEtudiant(etudiant);
                    newNote2.setEvaluation(evaluationRepo.findByCode(CodeEva.TPE).get());
                    newNote2.setIsFinal(true);
                    newNote2.setAnneeAcademique(noteList.get(0).getAnneeAcademique());

                    noteRepo.save(newNote2);
                }

                Note newNote3 = new Note();
                if (sommeTP == -1.0){
                    newNote3.setSessions(0);
                    newNote3.setCours(cours.get());
                    newNote3.setEtudiant(etudiant);
                    newNote3.setEvaluation(evaluationRepo.findByCode(CodeEva.TP).get());
                    newNote3.setIsFinal(true);
                    newNote3.setAnneeAcademique(noteList.get(0).getAnneeAcademique());

                    noteRepo.save(newNote3);
                }else {
                    valeurTP = sommeTP/creditCours;
                    Double resultTP = Math.round(valeurTP*100.0)/100.0;
                    newNote3.setValeur(resultTP);
                    newNote3.setSessions(0);
                    newNote3.setCours(cours.get());
                    newNote3.setEtudiant(etudiant);
                    newNote3.setEvaluation(evaluationRepo.findByCode(CodeEva.TP).get());
                    newNote3.setIsFinal(true);
                    newNote3.setAnneeAcademique(noteList.get(0).getAnneeAcademique());

                    noteRepo.save(newNote3);
                }


            }else if (type.equals("CC, TPE, EE")){
                Note newNote1 = new Note();
                if (sommeCC == -1.0){
                    newNote1.setSessions(0);
                    newNote1.setCours(cours.get());
                    newNote1.setEtudiant(etudiant);
                    newNote1.setEvaluation(evaluationRepo.findByCode(CodeEva.CC).get());
                    newNote1.setIsFinal(true);
                    newNote1.setAnneeAcademique(anneeAcademique);

                    noteRepo.save(newNote1);
                }else {
                    valeurCC = sommeCC/creditCours;
                    double resultCC = Math.round(valeurCC*100.0)/100.0;

                    newNote1.setValeur(resultCC);
                    newNote1.setSessions(0);
                    newNote1.setCours(cours.get());
                    newNote1.setEtudiant(etudiant);
                    newNote1.setEvaluation(evaluationRepo.findByCode(CodeEva.CC).get());
                    newNote1.setIsFinal(true);
                    newNote1.setAnneeAcademique(anneeAcademique);

                    noteRepo.save(newNote1);
                }

                Note newNote2 = new Note();
                if (sommeTPE == -1.0){
                    newNote2.setSessions(0);
                    newNote2.setCours(cours.get());
                    newNote2.setEtudiant(etudiant);
                    newNote2.setEvaluation(evaluationRepo.findByCode(CodeEva.TPE).get());
                    newNote2.setIsFinal(true);
                    newNote2.setAnneeAcademique(noteList.get(0).getAnneeAcademique());

                    noteRepo.save(newNote2);
                }else {
                    valeurTPE = sommeTPE/creditCours;
                    double resultTPE = Math.round(valeurTPE*100.0)/100.0;
                    newNote2.setValeur(resultTPE);
                    newNote2.setSessions(0);
                    newNote2.setCours(cours.get());
                    newNote2.setEtudiant(etudiant);
                    newNote2.setEvaluation(evaluationRepo.findByCode(CodeEva.TPE).get());
                    newNote2.setIsFinal(true);
                    newNote2.setAnneeAcademique(noteList.get(0).getAnneeAcademique());

                    noteRepo.save(newNote2);
                }

            }else if (type.equals("CC, EE")){
                Note newNote1 = new Note();
                if (sommeCC == -1.0){
                    newNote1.setSessions(0);
                    newNote1.setCours(cours.get());
                    newNote1.setEtudiant(etudiant);
                    newNote1.setEvaluation(evaluationRepo.findByCode(CodeEva.CC).get());
                    newNote1.setIsFinal(true);
                    newNote1.setAnneeAcademique(anneeAcademique);

                    noteRepo.save(newNote1);
                }else {
                    valeurCC = sommeCC/creditCours;
                    double resultCC = Math.round(valeurCC*100.0)/100.0;

                    newNote1.setValeur(resultCC);
                    newNote1.setSessions(0);
                    newNote1.setCours(cours.get());
                    newNote1.setEtudiant(etudiant);
                    newNote1.setEvaluation(evaluationRepo.findByCode(CodeEva.CC).get());
                    newNote1.setIsFinal(true);
                    newNote1.setAnneeAcademique(anneeAcademique);

                    noteRepo.save(newNote1);
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

        Cours cours = coursRepo.findByCoursId(note.getCours().getCoursId());
        TypeCours typeCours = typeCoursRepo.findById(cours.getTypecours().getId()).orElse(null);
        Evaluation evaluation = evaluationRepo.findById(note.getEvaluation().getId()).orElse(null);

        if (typeCours == null || evaluation == null) {
            return null;
        }

        String type = typeCours.getNom();
        boolean isValidType = (type.equals("CC, TPE, EE") && evaluation.getCode().equals(CodeEva.TP)) ||
                (type.equals("CC, EE") && (evaluation.getCode().equals(CodeEva.TP) || evaluation.getCode().equals(CodeEva.TPE)));

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

        Cours cours = coursRepo.findByCoursId(note.getCours().getCoursId());
        TypeCours typeCours = typeCoursRepo.findById(cours.getTypecours().getId()).orElse(null);
        Evaluation evaluation = evaluationRepo.findById(note.getEvaluation().getId()).orElse(null);

        if (typeCours == null || evaluation == null) {
            return null;
        }

        String type = typeCours.getNom();
        boolean isValidType = (type.equals("CC, TPE, EE") && evaluation.getCode().equals(CodeEva.TP)) ||
                (type.equals("CC, EE") && (evaluation.getCode().equals(CodeEva.TP) || evaluation.getCode().equals(CodeEva.TPE)));

        if (isValidType) {
            return null;
        }
//        if (note.getValeur() == null){
//            note.setValeur(null);
//        }

        return noteRepo.save(note);
    }

    public Note ajouterNoteExamen(String valeur, Double noteUE) {

        Anonymat anonymat = anonymatRepo.findByValeur(valeur);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findById(anonymat.getAnneeAcademique().getId()).orElse(null);
        Etudiant etudiant = etudiantRepo.findById(anonymat.getEtudiant().getId()).orElse(null);
        Cours cours = coursRepo.findByCoursId(anonymat.getCours().getCoursId());
        Module module = moduleRepo.findById(cours.getCoursId()).orElse(null);
        Evaluation evaluation = evaluationRepo.findByCode(CodeEva.EE).orElse(null);

        if (anneeAcademique == null || etudiant == null || cours == null || evaluation == null) {
            return null;
        }
            Note note = new Note();

            note.setEvaluation(evaluation);
            note.setModule(module);
            note.setEtudiant(etudiant);
            note.setCours(cours);
            note.setSessions(anonymat.getSessions());
            if (noteUE != -1.0){
                note.setValeur(noteUE);
            }
            note.setAnneeAcademique(anneeAcademique);
            note.setIsFinal(true);

            return noteRepo.save(note);
    }

    public Double moyenneSemestre(Long id, int annee, int semestre, String code) {
        Etudiant etudiant = etudiantRepo.findById(id).orElse(null);
        Departement departement = departementRepo.findByCode(code).orElse(null);

        if (etudiant == null || departement == null) {
            return null;
        }

        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(annee);

        Semestre semestreEntity = semestreRepo.findByValeur(semestre);

        if (semestreEntity == null) {
            return null;
        }

        Double sum = 0.0;
        int creditTotal = 0;

        for (Cours cours : coursRepo.findBySemestreAndDepartement(semestreEntity, departement)) {
            Double moyenneCours = calculMoyenneCours(etudiant, cours, anneeAcademique);
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

    private Double calculMoyenneCours(Etudiant etudiant, Cours cours, AnneeAcademique anneeAcademique) {
        Double noteExamen = -1.0, noteCC = -1.0, noteTPE = -1.0, noteTP = -1.0;

        for (Note note : noteRepo.findByCoursAndEtudiantAndAnneeAcademiqueAndIsFinal(cours, etudiant, anneeAcademique, true)) {
            Evaluation evaluation = note.getEvaluation();
            switch (evaluation.getCode()) {
                case EE:
                    noteExamen = (note.getValeur() != null) ? note.getValeur() : 0.0;
                    break;
                case CC:
                    noteCC = (note.getValeur() != null) ? note.getValeur() : 0.0;
                    break;
                case TPE:
                    noteTPE = (note.getValeur() != null) ? note.getValeur() : 0.0;
                    break;
                case TP:
                    noteTP = (note.getValeur() != null) ? note.getValeur() : 0.0;
                    break;
            }
        }

        TypeCours typeCours = typeCoursRepo.findById(cours.getTypecours().getId()).orElse(null);

        if (typeCours == null) {
            return -1.0;
        }

        switch (typeCours.getNom()) {
            case "CC, TPE, TP, Examen":
                return 0.7 * noteExamen + 0.1 * noteCC + 0.1 * noteTPE + 0.1 * noteTP;
            case "CC, TPE, Examen":
                return 0.7 * noteExamen + 0.2 * noteCC + 0.1 * noteTPE;
            case "CC, Examen":
                return 0.7 * noteExamen + 0.3 * noteCC;
            default:
                return -1.0;
        }
    }

    public Double moyenneAnnuelle(Long id, int annee, String code){
        Double semestre1 = moyenneSemestre(id, annee, 1, code);
        Double semestre2 = moyenneSemestre(id, annee, 2, code);
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
        Etudiant etudiant = etudiantRepo.findById(id).orElse(null);
        Departement departement = departementRepo.findByCode(code).orElse(null);

        if (etudiant == null || departement == null) {
            return 0;
        }

        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(annee);

        Semestre semestreEntity = semestreRepo.findByValeur(semestre);

        if (semestreEntity == null) {
            return 0;
        }
        for (Cours cours : coursRepo.findBySemestreAndDepartement(semestreEntity, departement)) {
            Double moyenneCours = calculMoyenneCours(etudiant, cours, anneeAcademique);
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

    public List<Etudiant> getListPassageByParcours(String label, int year, TYPE type){

        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Parcours parcours = parcoursRepo.findByLabel(label).orElse(null);
        Niveau niveau = niveauRepo.findById(parcours.getNiveau().getId()).orElse(null);
        Option option = optionRepo.findById(parcours.getOption().getId()).orElse(null);
        Departement departement = departementRepo.findById(option.getDepartement().getId()).orElse(null);
        List<Etudiant> passageList = new ArrayList<>();
        if (anneeAcademique == null || parcours == null || niveau == null || option == null || departement == null){
            return null;
        }
        List<Etudiant> etudiantList = etudiantService.getListEtudiantByParcours(parcours.getLabel(), anneeAcademique.getNumeroDebut(), type);
        if (etudiantList.isEmpty()){
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
                        etudiantRepo.save(etudiant);
                    }else if (creditTotal == 12){
                        etudiant.setValide(true);
                        etudiant.setValideAll(true);
                        etudiantRepo.save(etudiant);
                    }
                }
            }else if ((creditTotal >= 9) && (niveau.getValeur().equals(1))){
                passageList.add(etudiant);

                if (creditTotal < 12){
                    etudiant.setValide(true);
                    etudiant.setValideAll(false);
                    etudiantRepo.save(etudiant);
                }else if (creditTotal == 12){
                    etudiant.setValide(true);
                    etudiant.setValideAll(true);
                    etudiantRepo.save(etudiant);
                }
            }else {
                if (creditTotal < 12){
                    etudiant.setValide(false);
                    etudiant.setValideAll(false);
                    etudiantRepo.save(etudiant);
                }else if (creditTotal == 12){
                    etudiant.setValide(true);
                    etudiant.setValideAll(true);
                    Etudiant etud = etudiantRepo.save(etudiant);
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
        return (20*note)/100;
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

    public List<Note> getNotesEtudiantByModule(Long id, int year, String code) {
        Etudiant etudiant = etudiantRepo.findById(id).orElse(null);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Module module = moduleRepo.findByCode(code).orElse(null);

        if (etudiant == null || anneeAcademique == null || module == null) {
            return null;
        }

        List<Note> noteList = noteRepo.findAllByEtudiantAndAnneeAcademiqueAndModule(etudiant, anneeAcademique, module);
        List<Note> filteredNotes = filterNotesModule(noteList, etudiant, anneeAcademique, module);

        return filteredNotes;
    }
}