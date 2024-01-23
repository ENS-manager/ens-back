package com.api.gestnotesapi.controllers;

import com.api.gestnotesapi.entities.*;
import com.api.gestnotesapi.entities.Module;
import com.api.gestnotesapi.repository.*;
import com.api.gestnotesapi.servicesImpl.EtudiantServiceImpl;
import com.api.gestnotesapi.servicesImpl.NotesModulesCoursServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api/v1/admin/note")
public class NoteController {

    @Autowired
    private NoteRepo noteRepo;
    @Autowired
    private CoursRepo coursRepo;
    @Autowired
    private AnneeAcademiqueRepo anneeAcademiqueRepo;
    @Autowired
    private EvaluationRepo evaluationRepo;
    @Autowired
    private ModuleRepo moduleRepo;
    @Autowired
    private CreditRepo creditRepo;
    @Autowired
    private EtudiantRepo etudiantRepo;
    @Autowired
    private ParcoursRepo parcoursRepo;
    @Autowired
    private InscriptionRepo inscriptionRepo;
    @Autowired
    private AnonymatRepo anonymatRepo;
    @Autowired
    private TypeCoursRepo typeCoursRepo;
    @Autowired
    private SemestreRepo semestreRepo;
    @Autowired
    private NiveauRepo niveauRepo;
    @Autowired
    private CycleRepo cycleRepo;

    //  Ajouter une Note
    @PostMapping("/addNoteCours")
    public ResponseEntity<Note> saveNoteCours(@RequestBody Note note){

        if (note == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        note.setIsFinal(true);
        Cours cours = coursRepo.findByCoursId(note.getCours().getCoursId());
        TypeCours typeCours = typeCoursRepo.findById(cours.getTypecours().getId()).get();
        String type = typeCours.getNom();
        Evaluation evaluation = evaluationRepo.findById(note.getEvaluation().getId()).get();
        if ((type.equals("CC, TPE, EE")) && (evaluation.getCode().equals("TP"))){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }else if ((type.equals("CC, EE")) && ((evaluation.getCode().equals("TP")) || (evaluation.getCode().equals("TPE")))){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(noteRepo.save(note), HttpStatus.OK);
    }

    @PostMapping("/addNoteModule")
    public ResponseEntity<Note> saveNoteModule(@RequestBody Note note){

        if (note == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Cours cours = coursRepo.findByCoursId(note.getCours().getCoursId());
        TypeCours typeCours = typeCoursRepo.findById(cours.getTypecours().getId()).get();
        String type = typeCours.getNom();
        Evaluation evaluation = evaluationRepo.findById(note.getEvaluation().getId()).get();
        if ((type.equals("CC, TPE, EE")) && (evaluation.getCode().equals("TP"))){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }else if ((type.equals("CC, EE")) && ((evaluation.getCode().equals("TP")) || (evaluation.getCode().equals("TPE")))){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(noteRepo.save(note), HttpStatus.OK);
    }

//    Ajouter une note d'examen avec comme entrees: le numero d'anonymat et la note
    @PostMapping("/addNoteExamen/anonymat/{valeur}/note")
    public ResponseEntity<Note> saveNoteExamen(@PathVariable String valeur, @RequestParam("note") double noteUE){

        Anonymat anonymat = anonymatRepo.findByValeur(valeur);
        AnneeAcademique anneeAcademique = new AnneeAcademique();
        Etudiant etudiant = new Etudiant();
        Cours cours = new Cours();
        Module module = new Module();
        Evaluation evaluation = new Evaluation();
        Note note = new Note();
        int session = 0;
        anneeAcademique = anneeAcademiqueRepo.findById(anonymat.getAnneeAcademique().getId()).get();
        etudiant = etudiantRepo.findById(anonymat.getEtudiant().getId()).get();
        cours = coursRepo.findByCoursId(anonymat.getCours().getCoursId());
        session = anonymat.getSessions();
        for (Module mod : moduleRepo.findAll()){
            Cours cour = coursRepo.findByCoursId(mod.getCours().getCoursId());
            if (cour.getCode().equals(cours.getCode())){
                module = mod;
                break;
            }
        }
        evaluation = evaluationRepo.findByCode("EE").get();

        note.setEvaluation(evaluation);
        note.setModule(module);
        note.setEtudiant(etudiant);
        note.setCours(cours);
        note.setSessions(session);
        note.setValeur(noteUE);
        note.setAnneeAcademique(anneeAcademique);
        note.setIsFinal(true);

        return new ResponseEntity<>(noteRepo.save(note), HttpStatus.OK);
    }

    //    Liste des Notes
    @GetMapping("/findAllNote")
    public ResponseEntity<List<Note>> getAllNote() {

        List<Note> list = noteRepo.findAll();
        if (list == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //    Une Note par id
    @GetMapping("/findNoteById/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable("id") Long id) {

        Note note = noteRepo.findById(id).orElse(null);
        if (note == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(note, HttpStatus.OK);
    }

    //    Moyenne ponderee des notes de cc, tpe et tp d'une session, pour une annee academique et un cours
    @GetMapping("/findMoyenneNotesModuleByAnneeAca/{year}/codeUE")
    public ResponseEntity<String> getNoteByCode(@PathVariable int year, @RequestParam("code") String code){

        List<Note> noteList = new ArrayList<>();
//        List<Etudiant> etudiantList = new ArrayList<>();
        Optional<Cours> cours = coursRepo.findByCode(code);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        for (Note note : noteRepo.findAll()){
            Cours cour = coursRepo.findByCoursId(note.getCours().getCoursId());
            Module module = moduleRepo.findById(note.getModule().getId()).get();
            Cours coursModule = coursRepo.findByCoursId(module.getCours().getCoursId());
            AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(note.getAnneeAcademique().getId()).get();
            if ((coursModule.getCode().equals(cours.get().getCode()))
                    && (note.getIsFinal().equals(false))
                    && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
            ){

                noteList.add(note);
            }
        }

//        List<Note> pv = new ArrayList<>();
        Credit credit = creditRepo.findById(cours.get().getCredit().getId()).get();
        int creditCours = credit.getValeur();

        TypeCours typeCours = typeCoursRepo.findById(cours.get().getTypecours().getId()).get();
        String type = typeCours.getNom();
        for (Etudiant etudiant : etudiantRepo.findAll()){
            double sommeCC = 0.0, sommeTPE = 0.0, sommeTP = 0.0;
            for (Note note : noteList){
                Evaluation evaluation = evaluationRepo.findById(note.getEvaluation().getId()).get();
                Module module = moduleRepo.findById(note.getModule().getId()).get();
                Credit creditModule = creditRepo.findById(module.getCredit().getId()).get();
                Etudiant etud = etudiantRepo.findById(note.getEtudiant().getId()).get();
                if (type.equals("CC, TPE, TP, EE")){

                    if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("CC"))){
                        sommeCC = (creditModule.getValeur()*note.getValeur()) + sommeCC;
                    }else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("TPE"))){
                        sommeTPE = (creditModule.getValeur()*note.getValeur()) + sommeTPE;
                    }else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("TP"))){
                        sommeTP = (creditModule.getValeur()*note.getValeur()) + sommeTP;
                    }

                }else if (type.equals("CC, TPE, EE")){

                    if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("CC"))){
                        sommeCC = (creditModule.getValeur()*note.getValeur()) + sommeCC;
                    }else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("TPE"))){
                        sommeTPE = (creditModule.getValeur()*note.getValeur()) + sommeTPE;
                    }

                }else if (type.equals("CC, EE")){
                    if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("CC"))) {
                        sommeCC = (creditModule.getValeur() * note.getValeur()) + sommeCC;
                    }
                }


            }
            double valeurCC = 0.0, valeurTPE = 0.0,valeurTP = 0.0;
            if (type.equals("CC, TPE, TP, EE")){
                valeurCC = sommeCC/creditCours;
                double resultCC = Math.round(valeurCC*100.0)/100.0;
                Note newNote1 = new Note();

                newNote1.setValeur(resultCC);
                newNote1.setSessions(0);
                newNote1.setCours(cours.get());
                newNote1.setEtudiant(etudiant);
                newNote1.setEvaluation(evaluationRepo.findByCode("CC").get());
                newNote1.setIsFinal(true);
//                newNote1.setModule(noteList.get(0).getModule());
                newNote1.setAnneeAcademique(anneeAcademique);

                noteRepo.save(newNote1);

                valeurTPE = sommeTPE/creditCours;
                double resultTPE = Math.round(valeurTPE*100.0)/100.0;
                Note newNote2 = new Note();

                newNote2.setValeur(resultTPE);
                newNote2.setSessions(0);
                newNote2.setCours(cours.get());
                newNote2.setEtudiant(etudiant);
                newNote2.setEvaluation(evaluationRepo.findByCode("TPE").get());
                newNote2.setIsFinal(true);
//                newNote2.setModule(noteList.get(0).getModule());
                newNote2.setAnneeAcademique(noteList.get(0).getAnneeAcademique());

                noteRepo.save(newNote2);

                valeurTP = sommeTP/creditCours;
                double resultTP = Math.round(valeurTP*100.0)/100.0;
                Note newNote3 = new Note();

                newNote3.setValeur(resultTP);
                newNote3.setSessions(0);
                newNote3.setCours(cours.get());
                newNote3.setEtudiant(etudiant);
                newNote3.setEvaluation(evaluationRepo.findByCode("TP").get());
                newNote3.setIsFinal(true);
//                newNote3.setModule(noteList.get(0).getModule());
                newNote3.setAnneeAcademique(noteList.get(0).getAnneeAcademique());

                noteRepo.save(newNote3);

            }else if (type.equals("CC, TPE, EE")){
                valeurCC = sommeCC/creditCours;
                double resultCC = Math.round(valeurCC*100.0)/100.0;
                Note newNote1 = new Note();

                newNote1.setValeur(resultCC);
                newNote1.setSessions(0);
                newNote1.setCours(cours.get());
                newNote1.setEtudiant(etudiant);
                newNote1.setEvaluation(evaluationRepo.findByCode("CC").get());
                newNote1.setIsFinal(true);
//                newNote1.setModule(noteList.get(0).getModule());
                newNote1.setAnneeAcademique(anneeAcademique);

                noteRepo.save(newNote1);

                valeurTPE = sommeTPE/creditCours;
                double resultTPE = Math.round(valeurTPE*100.0)/100.0;
                Note newNote2 = new Note();

                newNote2.setValeur(resultTPE);
                newNote2.setSessions(0);
                newNote2.setCours(cours.get());
                newNote2.setEtudiant(etudiant);
                newNote2.setEvaluation(evaluationRepo.findByCode("TPE").get());
                newNote2.setIsFinal(true);
//                newNote2.setModule(noteList.get(0).getModule());
                newNote2.setAnneeAcademique(noteList.get(0).getAnneeAcademique());

                noteRepo.save(newNote2);

            }else if (type.equals("CC, EE")){
                valeurCC = sommeCC/creditCours;
                double resultCC = Math.round(valeurCC*100.0)/100.0;
                Note newNote1 = new Note();

                newNote1.setValeur(resultCC);
                newNote1.setSessions(0);
                newNote1.setCours(cours.get());
                newNote1.setEtudiant(etudiant);
                newNote1.setEvaluation(evaluationRepo.findByCode("CC").get());
                newNote1.setIsFinal(true);
//                newNote1.setModule(noteList.get(0).getModule());
                newNote1.setAnneeAcademique(anneeAcademique);

                noteRepo.save(newNote1);
            }
        }

        return new ResponseEntity<>("Opération effectuée avec succès", HttpStatus.OK);
    }

    //    Moyenne de l'etudiant pour une session, une annee academique et un cours
    @GetMapping("/findMoyenneCoursFromEtudiant/{id}/session/{session}/anneeAca/{year}/codeUE")
    public ResponseEntity<Double> getMoyenneEtudiantCours(@PathVariable Long id, @PathVariable int session, @PathVariable int year, @RequestParam String code){

        List<Note> noteList = new ArrayList<>();
        Optional<Cours> cours = coursRepo.findByCode(code);
        Etudiant etudiant = etudiantRepo.findById(id).get();
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        for (Note note : noteRepo.findAll()){
            Cours cour = coursRepo.findByCoursId(note.getCours().getCoursId());
            AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(note.getAnneeAcademique().getId()).get();
            if ((cour.getCode().equals(cours.get().getCode()))
                    && (note.getIsFinal().equals(true))
                    && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
            ){

                noteList.add(note);
            }
        }

        double sommeCC = -1.0, sommeTPE = -1.0, sommeTP = -1.0, noteEE = -1.0, moy = 0.0, ccSurTrente = 0.0;
        TypeCours typeCours = typeCoursRepo.findById(cours.get().getTypecours().getId()).get();
        String type = typeCours.getNom();

        for (Note note : noteList) {
            Evaluation evaluation = evaluationRepo.findById(note.getEvaluation().getId()).get();
            Etudiant etud = etudiantRepo.findById(note.getEtudiant().getId()).get();

            if (type.equals("CC, TPE, TP, EE")){

                if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("CC"))) {
                    sommeCC = note.getValeur();
                } else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("TPE"))) {
                    sommeTPE = note.getValeur();
                } else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("TP"))) {
                    sommeTP = note.getValeur();
                } else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("EE")) && (note.getSessions().equals(session))) {
                    noteEE = note.getValeur();
                }

            }else if (type.equals("CC, TPE, EE")){

                if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("CC"))) {
                    sommeCC = note.getValeur();
                } else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("TPE"))) {
                    sommeTPE = note.getValeur();
                } else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("EE")) && (note.getSessions().equals(session))) {
                    noteEE = note.getValeur();
                }

            }else {
                if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("CC"))) {
                    sommeCC = note.getValeur();
                } else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("EE")) && (note.getSessions().equals(session))) {
                    noteEE = note.getValeur();
                }
            }
        }

        if (type.equals("CC, EE")){
            if ((sommeCC == -1.0) || (noteEE == -1.0)){
                moy = -1.0;
            }else{
                ccSurTrente = (30*sommeCC)/20;
                moy = noteEE + ccSurTrente;
            }
        }else if (type.equals("CC, TPE, EE")){
            if ((sommeCC == -1.0) || (noteEE == -1.0) || (sommeTPE == -1.0)){
                moy = -1.0;
            }else{
                double a = sommeCC + sommeTPE;
                ccSurTrente = (30*a)/20;
                moy = noteEE + ccSurTrente;
            }
        }else {
            if ((sommeCC == -1.0) || (noteEE == -1.0) || (sommeTPE == -1.0) || (sommeTP == -1.0)){
                moy = -1.0;
            }else{
                double a = sommeCC + sommeTPE + sommeTP;
                ccSurTrente = (30*a)/20;
                moy = noteEE + ccSurTrente;
            }
        }

        double result = 0.0;
        if (moy == -1.0){
            result = moy;
        }else {
            result = Math.round(moy*100.0)/100.0;
        }

//        double result = Math.round(moy*100.0)/100.0;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //    Decision sur un etudiant pour une session, une annee academique et un cours
    @GetMapping("/findDecisionOnEtudiant/{id}/session/{session}/anneeAca/{year}/codeUE")
    public ResponseEntity<String> getDecisionOnEtudiant(@PathVariable Long id, @PathVariable int session, @PathVariable int year, @RequestParam String code){

        List<Note> noteList = new ArrayList<>();
        Optional<Cours> cours = coursRepo.findByCode(code);
        Etudiant etudiant = etudiantRepo.findById(id).get();
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        for (Note note : noteRepo.findAll()){
            Cours cour = coursRepo.findByCoursId(note.getCours().getCoursId());
            AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(note.getAnneeAcademique().getId()).get();
            if ((cour.getCode().equals(cours.get().getCode()))
                    && (note.getIsFinal().equals(true))
                    && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
            ){

                noteList.add(note);
            }
        }

        String decision = "";
        double sommeCC = -1.0, sommeTPE = -1.0, sommeTP = -1.0, noteEE = -1.0, moy = 0.0, ccSurTrente = 0.0;
        TypeCours typeCours = typeCoursRepo.findById(cours.get().getTypecours().getId()).get();
        String type = typeCours.getNom();

        for (Note note : noteList) {
            Evaluation evaluation = evaluationRepo.findById(note.getEvaluation().getId()).get();
            Etudiant etud = etudiantRepo.findById(note.getEtudiant().getId()).get();

            if (type.equals("CC, TPE, TP, EE")){

                if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("CC"))) {
                    sommeCC = note.getValeur();
                } else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("TPE"))) {
                    sommeTPE = note.getValeur();
                } else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("TP"))) {
                    sommeTP = note.getValeur();
                } else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("EE")) && (note.getSessions().equals(session))) {
                    noteEE = note.getValeur();
                }

            }else if (type.equals("CC, TPE, EE")){

                if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("CC"))) {
                    sommeCC = note.getValeur();
                } else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("TPE"))) {
                    sommeTPE = note.getValeur();
                } else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("EE")) && (note.getSessions().equals(session))) {
                    noteEE = note.getValeur();
                }

            }else {
                if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("CC"))) {
                    sommeCC = note.getValeur();
                } else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("EE")) && (note.getSessions().equals(session))) {
                    noteEE = note.getValeur();
                }
            }
        }

        if (type.equals("CC, EE")){
            if ((sommeCC == -1.0) || (noteEE == -1.0)){
                moy = -1.0;
            }else{
                ccSurTrente = (30*sommeCC)/20;
                moy = noteEE + ccSurTrente;
            }
        }else if (type.equals("CC, TPE, EE")){
            if ((sommeCC == -1.0) || (noteEE == -1.0) || (sommeTPE == -1.0)){
                moy = -1.0;
            }else{
                double a = sommeCC + sommeTPE;
                ccSurTrente = (30*a)/20;
                moy = noteEE + ccSurTrente;
            }
        }else {
            if ((sommeCC == -1.0) || (noteEE == -1.0) || (sommeTPE == -1.0) || (sommeTP == -1.0)){
                moy = -1.0;
            }else{
                double a = sommeCC + sommeTPE + sommeTP;
                ccSurTrente = (30*a)/20;
                moy = noteEE + ccSurTrente;
            }
        }

        if (moy == -1.0){
            decision = "EL";
        }else if(moy >= 10.0){
            decision = "CA";
        }else if((moy >= 8.0) && (moy <= 9.99)){
            decision = "CANT";
        }else if(moy <= 7.99){
            decision = "NCA";
        }
        return new ResponseEntity<>(decision, HttpStatus.OK);
    }

    //    Moyenne de CC sur 30 d'un etudiant pour une annee academique et un cours
    @GetMapping("/findMoyenneCCFromEtudiant/{id}/anneeAca/{year}/codeUE")
    public ResponseEntity<Double> getMoyenneEtudiantCcCours(@PathVariable Long id, @PathVariable int year, @RequestParam String code){

        List<Note> noteList = new ArrayList<>();
        Optional<Cours> cours = coursRepo.findByCode(code);
        Etudiant etudiant = etudiantRepo.findById(id).get();
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        for (Note note : noteRepo.findAll()){
            Cours cour = coursRepo.findByCoursId(note.getCours().getCoursId());
            AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(note.getAnneeAcademique().getId()).get();
            if ((cour.getCode().equals(cours.get().getCode()))
                    && (note.getIsFinal().equals(true))
                    && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
            ){

                noteList.add(note);
            }
        }

        double sommeCC = -1.0, sommeTPE = -1.0, sommeTP = -1.0, ccSurTrente = 0.0;
        TypeCours typeCours = typeCoursRepo.findById(cours.get().getTypecours().getId()).get();
        String type = typeCours.getNom();

        for (Note note : noteList) {
            Evaluation evaluation = evaluationRepo.findById(note.getEvaluation().getId()).get();
            Etudiant etud = etudiantRepo.findById(note.getEtudiant().getId()).get();

            if (type.equals("CC, TPE, TP, EE")){

                if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("CC"))) {
                    sommeCC = note.getValeur();
                } else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("TPE"))) {
                    sommeTPE = note.getValeur();
                } else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("TP"))) {
                    sommeTP = note.getValeur();
                }

            }else if (type.equals("CC, TPE, EE")){

                if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("CC"))) {
                    sommeCC = note.getValeur();
                } else if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("TPE"))) {
                    sommeTPE = note.getValeur();
                }

            }else {
                if ((etud.getMatricule().equals(etudiant.getMatricule())) && (evaluation.getCode().equals("CC"))) {
                    sommeCC = note.getValeur();
                }
            }
        }

        if (type.equals("CC, EE")){
            if (sommeCC == -1.0){
                ccSurTrente = -1.0;
            }else{
                ccSurTrente = (30*sommeCC)/20;
            }
        }else if (type.equals("CC, TPE, EE")){
            if ((sommeCC == -1.0) && (sommeTPE == -1.0)){
                ccSurTrente = -1.0;
            }else{
                double a = sommeCC + sommeTPE + 1.0;
                ccSurTrente = (30*a)/20;
            }
        }else {
            if ((sommeCC == -1.0) && (sommeTPE == -1.0) && (sommeTP == -1.0)){
                ccSurTrente = -1.0;
            }else if ((sommeCC == -1.0) && (sommeTPE != -1.0) && (sommeTP != -1.0)){
                double a = sommeTPE + sommeTP;
                ccSurTrente = (30*a)/20;
            }else if ((sommeCC != -1.0) && (sommeTPE != -1.0) && (sommeTP == -1.0)){
                double a = sommeTPE + sommeCC;
                ccSurTrente = (30*a)/20;
            }else if ((sommeCC != -1.0) && (sommeTPE == -1.0) && (sommeTP != -1.0)){
                double a = sommeCC + sommeTP;
                ccSurTrente = (30*a)/20;
            }else if ((sommeCC == -1.0) && (sommeTPE == -1.0) && (sommeTP != -1.0)){
                double a = sommeTP;
                ccSurTrente = (30*a)/20;
            }else if ((sommeCC == -1.0) && (sommeTPE != -1.0) && (sommeTP == -1.0)){
                double a = sommeTPE;
                ccSurTrente = (30*a)/20;
            }else if ((sommeCC != -1.0) && (sommeTPE == -1.0) && (sommeTP == -1.0)){
                double a = sommeCC;
                ccSurTrente = (30*a)/20;
            }
        }
        double result = Math.round(ccSurTrente*100.0)/100.0;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
//    revoir

//    Moyenne semestrielle d'un etudiant pour une annee academique
    @GetMapping("/findMoyenneSemestrielleFromEtudiant/{id}/anneeAca/{year}/semestre/{valeur}")
    public ResponseEntity<Double> getMoyenneSemestrielle(@PathVariable Long id, @PathVariable int year, @PathVariable int valeur){

        Semestre semestre = semestreRepo.findByValeur(valeur);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Etudiant etudiant = etudiantRepo.findById(id).get();

        double sum = 0.0;
        int creditTotal = 0;

        for (Cours cours : coursRepo.findAll()){
            double sommeCC = 0.0, sommeTPE = 0.0, sommeTP = 0.0, noteEE = 0.0, moy = 0.0, prod = 0.0;
            List<Note> noteExamenList = new ArrayList<>();
            TypeCours typeCours = typeCoursRepo.findById(cours.getTypecours().getId()).get();
            String type = typeCours.getNom();
            Semestre sem = semestreRepo.findById(cours.getSemestre().getId()).get();
            if (sem.getValeur().equals(semestre.getValeur())){
                for (Note note : noteRepo.findAll()) {
                    Evaluation evaluation = evaluationRepo.findById(note.getEvaluation().getId()).get();
                    Cours cour = coursRepo.findByCoursId(note.getCours().getCoursId());
                    AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(note.getAnneeAcademique().getId()).get();
                    Etudiant etud = etudiantRepo.findById(note.getEtudiant().getId()).get();
                    if (type.equals("CC, TPE, TP, EE")){

                        if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("CC"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeCC = note.getValeur();
                        } else if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("TPE"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeTPE = note.getValeur();
                        } else if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("TP"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeTP = note.getValeur();
                        } else if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("EE"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                            noteExamenList.add(note);
                        }

                    }else if (type.equals("CC, TPE, EE")){

                        if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("CC"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeCC = note.getValeur();
                        } else if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("TPE"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeTPE = note.getValeur();
                        } else if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("EE"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                            noteExamenList.add(note);
                        }

                    }else {
                        if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("CC"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeCC = note.getValeur();
                        } else if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("EE"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                            noteExamenList.add(note);
                        }
                    }

                }

                if (noteExamenList.size() > 1){
                    for (Note note : noteExamenList){
                        if (note.getSessions().equals(2)){
                            noteEE = note.getValeur();
                        }
                    }
                }else {
                    for (Note note : noteExamenList){
                        noteEE = note.getValeur();
                    }
                }

                if (type.equals("CC, EE")){
                    moy = 0.7*noteEE + 0.3*sommeCC;
                    prod = moy*cours.getCredit().getValeur();
                }else if (type.equals("CC, TPE, EE")){
                    moy = 0.7*noteEE + 0.2*sommeCC + 0.1*sommeTPE;
                    prod = moy*cours.getCredit().getValeur();
                }else {
                    moy = 0.7*noteEE + 0.1*sommeCC + 0.1*sommeTPE + 0.1*sommeTP;
                    prod = moy*cours.getCredit().getValeur();
                }
                creditTotal = creditTotal + cours.getCredit().getValeur();
            }
            sum = sum + prod;
        }

        double moySemestre = sum/creditTotal;
        double result = Math.round(moySemestre*100.0)/100.0;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

//    revoir
    //    Nombre de credit semestrielle d'un etudiant pour une annee academique
    @GetMapping("/findNombreCreditSemestrielleFromEtudiant/{id}/anneeAca/{year}/semestre/{valeur}")
    public ResponseEntity<Integer> getNombreCreditSemestrielle(@PathVariable Long id, @PathVariable int year, @PathVariable int valeur){

        Semestre semestre = semestreRepo.findByValeur(valeur);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Etudiant etudiant = etudiantRepo.findById(id).get();

        int nombreCredit = 0;
        for (Cours cours : coursRepo.findAll()){
            double sommeCC = 0.0, sommeTPE = 0.0, sommeTP = 0.0, noteEE = 0.0, moy = 0.0;
            List<Note> noteExamenList = new ArrayList<>();
            TypeCours typeCours = typeCoursRepo.findById(cours.getTypecours().getId()).get();
            String type = typeCours.getNom();
            Semestre sem = semestreRepo.findById(cours.getSemestre().getId()).get();
            if (sem.getValeur().equals(semestre.getValeur())){
                for (Note note : noteRepo.findAll()) {
                    Evaluation evaluation = evaluationRepo.findById(note.getEvaluation().getId()).get();
                    Cours cour = coursRepo.findByCoursId(note.getCours().getCoursId());
                    AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(note.getAnneeAcademique().getId()).get();
                    Etudiant etud = etudiantRepo.findById(note.getEtudiant().getId()).get();
                    if (type.equals("CC, TPE, TP, EE")){

                        if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("CC"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeCC = note.getValeur();
                        } else if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("TPE"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeTPE = note.getValeur();
                        } else if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("TP"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeTP = note.getValeur();
                        } else if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("EE"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                            noteExamenList.add(note);
                        }

                    }else if (type.equals("CC, TPE, EE")){

                        if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("CC"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeCC = note.getValeur();
                        } else if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("TPE"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeTPE = note.getValeur();
                        } else if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("EE"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                            noteExamenList.add(note);
                        }

                    }else {
                        if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("CC"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeCC = note.getValeur();
                        } else if ((cour.getCode().equals(cours.getCode()))
                                && (evaluation.getCode().equals("EE"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                            noteExamenList.add(note);
                        }
                    }

                }

                if (noteExamenList.size() > 1){
                    for (Note note : noteExamenList){
                        if (note.getSessions().equals(2)){
                            noteEE = note.getValeur();
                        }
                    }
                }else {
                    for (Note note : noteExamenList){
                        noteEE = note.getValeur();
                    }
                }

                if (type.equals("CC, EE")){
                    moy = 0.7*noteEE + 0.3*sommeCC;
                }else if (type.equals("CC, TPE, EE")){
                    moy = 0.7*noteEE + 0.2*sommeCC + 0.1*sommeTPE;
                }else {
                    moy = 0.7*noteEE + 0.1*sommeCC + 0.1*sommeTPE + 0.1*sommeTP;
                }

            }
            if (moy >= 10){
                nombreCredit = nombreCredit + cours.getCredit().getValeur();
            }
        }

        return new ResponseEntity<>(nombreCredit, HttpStatus.OK);
    }

//    revoir
    //    Moyenne annuelle d'un etudiant pour une annee academique
    @GetMapping("/findMoyenneAnnuelleFromEtudiant/{id}/anneeAca/{year}")
    public ResponseEntity<Double> getMoyenneAnnuelle(@PathVariable Long id, @PathVariable int year){

        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Etudiant etudiant = etudiantRepo.findById(id).get();

        double sum = 0.0;
        int creditTotal = 0;

        for (Cours cours : coursRepo.findAll()){
            double sommeCC = 0.0, sommeTPE = 0.0, sommeTP = 0.0, noteEE = 0.0, moy = 0.0, prod = 0.0;
            List<Note> noteExamenList = new ArrayList<>();
            TypeCours typeCours = typeCoursRepo.findById(cours.getTypecours().getId()).get();
            String type = typeCours.getNom();
            int credit = 0;
//            if (sem.getValeur().equals(semestre.getValeur())){
            for (Note note : noteRepo.findAll()) {
                Evaluation evaluation = evaluationRepo.findById(note.getEvaluation().getId()).get();
                Cours cour = coursRepo.findByCoursId(note.getCours().getCoursId());
                AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(note.getAnneeAcademique().getId()).get();
                Etudiant etud = etudiantRepo.findById(note.getEtudiant().getId()).get();
                if ((cour.getCode().equals(cours.getCode()))
                        && (type.equals("CC, TPE, TP, EE"))){
                    credit = cour.getCredit().getValeur();
                    if ((evaluation.getCode().equals("CC"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))
                            && (note.getIsFinal().equals(true))) {
                        sommeCC = note.getValeur();
                    } else if ((evaluation.getCode().equals("TPE"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))
                            && (note.getIsFinal().equals(true))) {
                        sommeTPE = note.getValeur();
                    } else if ((evaluation.getCode().equals("TP"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))
                            && (note.getIsFinal().equals(true))) {
                        sommeTP = note.getValeur();
                    } else if ((evaluation.getCode().equals("EE"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                        noteExamenList.add(note);
                    }

                }else if ((cour.getCode().equals(cours.getCode()))
                        && (type.equals("CC, TPE, EE"))){
                    credit = cour.getCredit().getValeur();
                    if ((evaluation.getCode().equals("CC"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))
                            && (note.getIsFinal().equals(true))) {
                        sommeCC = note.getValeur();
                    } else if ((evaluation.getCode().equals("TPE"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))
                            && (note.getIsFinal().equals(true))) {
                        sommeTPE = note.getValeur();
                    } else if ((evaluation.getCode().equals("EE"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                        noteExamenList.add(note);
                    }

                }else if ((cour.getCode().equals(cours.getCode()))
                        && (type.equals("CC, EE"))){
                    credit = cour.getCredit().getValeur();
                    if ((evaluation.getCode().equals("CC"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))
                            && (note.getIsFinal().equals(true))) {
                        sommeCC = note.getValeur();
                    } else if ((evaluation.getCode().equals("EE"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                        noteExamenList.add(note);
                    }
                }

            }
            if (noteExamenList.size() > 1){
                for (Note note : noteExamenList){
                    if (note.getSessions().equals(2)){
                        noteEE = note.getValeur();
                    }
                }
            }else {
                for (Note note : noteExamenList){
                    noteEE = note.getValeur();
                }
            }

            if (type.equals("CC, EE")){
                moy = 0.7*noteEE + 0.3*sommeCC;
                prod = moy*cours.getCredit().getValeur();
            }else if (type.equals("CC, TPE, EE")){
                moy = 0.7*noteEE + 0.2*sommeCC + 0.1*sommeTPE;
                prod = moy*cours.getCredit().getValeur();
            }else {
                moy = 0.7*noteEE + 0.1*sommeCC + 0.1*sommeTPE + 0.1*sommeTP;
                prod = moy*cours.getCredit().getValeur();
            }
            sum = sum + prod;
            creditTotal = creditTotal + credit;
//            }
        }
        double moyAnnuelle = sum/creditTotal;
        double result = Math.round(moyAnnuelle*100.0)/100.0;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

//    revoir
    //    Total credit annuelle d'un etudiant pour une annee academique
    @GetMapping("/findTotalCreditAnnuelleFromEtudiant/{id}/anneeAca/{year}")
    public ResponseEntity<Integer> getTotalCreditAnnuelle(@PathVariable Long id, @PathVariable int year){

        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Etudiant etudiant = etudiantRepo.findById(id).get();

        int creditTotal = 0;

        for (Cours cours : coursRepo.findAll()){
            double sommeCC = 0.0, sommeTPE = 0.0, sommeTP = 0.0, noteEE = 0.0, moy = 0.0;
            List<Note> noteExamenList = new ArrayList<>();
            TypeCours typeCours = typeCoursRepo.findById(cours.getTypecours().getId()).get();
            String type = typeCours.getNom();
            int credit = 0;
//            if (sem.getValeur().equals(semestre.getValeur())){
            for (Note note : noteRepo.findAll()) {
                Evaluation evaluation = evaluationRepo.findById(note.getEvaluation().getId()).get();
                Cours cour = coursRepo.findByCoursId(note.getCours().getCoursId());
                AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(note.getAnneeAcademique().getId()).get();
                Etudiant etud = etudiantRepo.findById(note.getEtudiant().getId()).get();
                if ((cour.getCode().equals(cours.getCode()))
                        && (type.equals("CC, TPE, TP, EE"))){
                    credit = cour.getCredit().getValeur();
                    if ((evaluation.getCode().equals("CC"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))
                            && (note.getIsFinal().equals(true))) {
                        sommeCC = note.getValeur();
                    } else if ((evaluation.getCode().equals("TPE"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))
                            && (note.getIsFinal().equals(true))) {
                        sommeTPE = note.getValeur();
                    } else if ((evaluation.getCode().equals("TP"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))
                            && (note.getIsFinal().equals(true))) {
                        sommeTP = note.getValeur();
                    } else if ((evaluation.getCode().equals("EE"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                        noteExamenList.add(note);
                    }

                }else if ((cour.getCode().equals(cours.getCode()))
                        && (type.equals("CC, TPE, EE"))){
                    credit = cour.getCredit().getValeur();
                    if ((evaluation.getCode().equals("CC"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))
                            && (note.getIsFinal().equals(true))) {
                        sommeCC = note.getValeur();
                    } else if ((evaluation.getCode().equals("TPE"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))
                            && (note.getIsFinal().equals(true))) {
                        sommeTPE = note.getValeur();
                    } else if ((evaluation.getCode().equals("EE"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                        noteExamenList.add(note);
                    }

                }else if ((cour.getCode().equals(cours.getCode()))
                        && (type.equals("CC, EE"))){
                    credit = cour.getCredit().getValeur();
                    if ((evaluation.getCode().equals("CC"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))
                            && (note.getIsFinal().equals(true))) {
                        sommeCC = note.getValeur();
                    } else if ((evaluation.getCode().equals("EE"))
                            && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                            && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                        noteExamenList.add(note);
                    }
                }

            }

            if (noteExamenList.size() > 1){
                for (Note note : noteExamenList){
                    if (note.getSessions().equals(2)){
                        noteEE = note.getValeur();
                    }
                }
            }else {
                for (Note note : noteExamenList){
                    noteEE = note.getValeur();
                }
            }

            if (type.equals("CC, EE")){
                moy = 0.7*noteEE + 0.3*sommeCC;
            }else if (type.equals("CC, TPE, EE")){
                moy = 0.7*noteEE + 0.2*sommeCC + 0.1*sommeTPE;
            }else {
                moy = 0.7*noteEE + 0.1*sommeCC + 0.1*sommeTPE + 0.1*sommeTP;
            }

            if (moy >= 10){
                creditTotal = creditTotal + credit;
            }
        }
        return new ResponseEntity<>(creditTotal, HttpStatus.OK);
    }

//    revoir
    //    Liste des passages de niveau par parcours et type d'etudiant
    @GetMapping("/findListPassageByParcours/{id}/anneeAca/{year}/etudiant")
    public ResponseEntity<List<Etudiant>> getListPassageByParcours(@PathVariable Long id, @PathVariable int year, @RequestParam("type") TYPE typeEtud){

        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Parcours parcours = parcoursRepo.findById(id).get();
        Niveau niveau = niveauRepo.findById(parcours.getNiveau().getId()).get();
        List<Etudiant> etudiantList = new ArrayList<>();
        List<Etudiant> passageList = new ArrayList<>();

        for (Inscription inscription : inscriptionRepo.findAll()){
            Parcours parcour = parcoursRepo.findById(inscription.getParcours().getId()).get();
            AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(inscription.getAnneeAcademique().getId()).get();
            if ((parcour.getLabel().equals(parcours.getLabel()))
                    && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                    && (inscription.getEtudiant().getType().equals(typeEtud))){
                Etudiant etudiant = etudiantRepo.findById(inscription.getEtudiant().getId()).get();
                etudiantList.add(etudiant);
            }
        }


        for (Etudiant etudiant : etudiantList){
            int creditTotal = 0;
            int creditSem1 = 0, creditSem2 = 0;
            for (Cours cours : coursRepo.findAll()){
                double sommeCC = 0.0, sommeTPE = 0.0, sommeTP = 0.0, noteEE = 0.0, moy = 0.0;
                List<Note> noteExamenList = new ArrayList<>();
                TypeCours typeCours = typeCoursRepo.findById(cours.getTypecours().getId()).get();
                String type = typeCours.getNom();
                int credit = 0;
                Semestre semestre = semestreRepo.findById(cours.getSemestre().getId()).get();
//            if (sem.getValeur().equals(semestre.getValeur())){
                for (Note note : noteRepo.findAll()) {
                    Evaluation evaluation = evaluationRepo.findById(note.getEvaluation().getId()).get();
                    Cours cour = coursRepo.findByCoursId(note.getCours().getCoursId());
                    AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(note.getAnneeAcademique().getId()).get();
                    Etudiant etud = etudiantRepo.findById(note.getEtudiant().getId()).get();
                    if ((cour.getCode().equals(cours.getCode()))
                            && (type.equals("CC, TPE, TP, EE"))){
                        credit = cour.getCredit().getValeur();
                        if ((evaluation.getCode().equals("CC"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeCC = note.getValeur();
                        } else if ((evaluation.getCode().equals("TPE"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeTPE = note.getValeur();
                        } else if ((evaluation.getCode().equals("TP"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeTP = note.getValeur();
                        } else if ((evaluation.getCode().equals("EE"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                            noteExamenList.add(note);
                        }

                    }else if ((cour.getCode().equals(cours.getCode()))
                            && (type.equals("CC, TPE, EE"))){
                        credit = cour.getCredit().getValeur();
                        if ((evaluation.getCode().equals("CC"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeCC = note.getValeur();
                        } else if ((evaluation.getCode().equals("TPE"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeTPE = note.getValeur();
                        } else if ((evaluation.getCode().equals("EE"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                            noteExamenList.add(note);
                        }

                    }else if ((cour.getCode().equals(cours.getCode()))
                            && (type.equals("CC, EE"))){
                        credit = cour.getCredit().getValeur();
                        if ((evaluation.getCode().equals("CC"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))
                                && (note.getIsFinal().equals(true))) {
                            sommeCC = note.getValeur();
                        } else if ((evaluation.getCode().equals("EE"))
                                && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                                && (etud.getMatricule().equals(etudiant.getMatricule()))) {
                            noteExamenList.add(note);
                        }
                    }

                }

                if (noteExamenList.size() > 1){
                    for (Note note : noteExamenList){
                        if (note.getSessions().equals(2)){
                            noteEE = note.getValeur();
                        }
                    }
                }else {
                    for (Note note : noteExamenList){
                        noteEE = note.getValeur();
                    }
                }

                if (type.equals("CC, EE")){
                    moy = 0.7*noteEE + 0.3*sommeCC;
                }else if (type.equals("CC, TPE, EE")){
                    moy = 0.7*noteEE + 0.2*sommeCC + 0.1*sommeTPE;
                }else {
                    moy = 0.7*noteEE + 0.1*sommeCC + 0.1*sommeTPE + 0.1*sommeTP;
                }

                if ((moy >= 10) && (semestre.getValeur().equals(1))){
                    creditSem1 = creditSem1 + credit;
                }else if ((moy >= 10) && (semestre.getValeur().equals(2))){
                    creditSem2 = creditSem2 + credit;
                }
            }
            creditTotal = creditSem1 + creditSem2;
//            if (niveau.getTerminal().equals(true)){
//                if ((creditTotal == 60) && (etudiant.getValideAll().equals(true)) && (etudiant.getValide().equals(true))){
//                    passageList.add(etudiant);
//                }
//            }
            int min = creditSem1 - 6;
            if ((niveau.getValeur().equals(2)) || (niveau.getValeur().equals(4))){
                if ((creditSem1 >= min) && (creditSem2 == 30) && (etudiant.getValideAll().equals(true)) && (etudiant.getValide().equals(true))){
                    passageList.add(etudiant);

                    int val = niveau.getValeur() + 1;
                    niveau.setValeur(val);
                    niveauRepo.save(niveau);
                    if (creditTotal < 60){
                        etudiant.setValide(true);
                        etudiant.setValideAll(false);
                        etudiantRepo.save(etudiant);
                    }else if (creditTotal == 60){
                        etudiant.setValide(true);
                        etudiant.setValideAll(true);
                        etudiantRepo.save(etudiant);
                    }
                }
            }else if ((creditTotal >= 45) && (niveau.getValeur().equals(1))){
                passageList.add(etudiant);

                int val = niveau.getValeur() + 1;
                niveau.setValeur(val);
                niveauRepo.save(niveau);
                if (creditTotal < 60){
                    etudiant.setValide(true);
                    etudiant.setValideAll(false);
                    etudiantRepo.save(etudiant);
                }else if (creditTotal == 60){
                    etudiant.setValide(true);
                    etudiant.setValideAll(true);
                    etudiantRepo.save(etudiant);
                }
            }
        }

        return new ResponseEntity<>(passageList, HttpStatus.OK);
    }

//    revoir
//    Notes d'un etudiant lors d'une session, une annee academique et un cours
    @GetMapping("/findNotesEtudiant/{id}/session/{session}/anneeAca/{year}/cours")
    public ResponseEntity<List<Note>> getNotesEtudiantByCours(@PathVariable Long id, @PathVariable int session, @PathVariable int year, @RequestParam("code") String code){

        Etudiant etudiant = etudiantRepo.findById(id).get();
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Cours cours = coursRepo.findByCode(code).get();
        List<Note> noteList = new ArrayList<>();

        for (Note note : noteRepo.findAll()){
            Evaluation evaluer = evaluationRepo.findById(note.getEvaluation().getId()).get();
            Etudiant etud = etudiantRepo.findById(note.getEtudiant().getId()).get();
            AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(note.getAnneeAcademique().getId()).get();
            Cours cour = coursRepo.findByCoursId(note.getCours().getCoursId());
            if (
                    (
                        (etud.getMatricule().equals(etudiant.getMatricule()))
                        && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                        && (cour.getCode().equals(cours.getCode()))
                        && (note.getIsFinal().equals(true))
                        && ((evaluer.getCode().equals("CC")) || ((evaluer.getCode().equals("TPE"))) || (evaluer.getCode().equals("TP")))
                    )
                            ||
                    (
                        (etud.getMatricule().equals(etudiant.getMatricule()))
                        && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                        && (cour.getCode().equals(cours.getCode()))
                        && (evaluer.getCode().equals("EE"))
                        && (note.getSessions().equals(session))
                        && (note.getIsFinal().equals(true))
                    )
            ){
                noteList.add(note);
            }
        }
//        double result = Math.round(valeur*100.0)/100.0;
        return new ResponseEntity<>(noteList, HttpStatus.OK);
    }

//    revoir
    //    Notes d'un etudiant pour une annee academique et un module
    @GetMapping("/findNotesEtudiant/{id}/anneeAca/{year}/module")
    public ResponseEntity<List<Note>> getNotesEtudiantByModule(@PathVariable Long id, @PathVariable int year, @RequestParam("code") String code){

        Etudiant etudiant = etudiantRepo.findById(id).get();
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        Module module = moduleRepo.findByCode(code).get();
        List<Note> noteList = new ArrayList<>();

        for (Note note : noteRepo.findAll()){
            Evaluation evaluer = evaluationRepo.findById(note.getEvaluation().getId()).get();
            Etudiant etud = etudiantRepo.findById(note.getEtudiant().getId()).get();
            AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(note.getAnneeAcademique().getId()).get();
            Module mod = moduleRepo.findById(note.getModule().getId()).get();
            if ((etud.getMatricule().equals(etudiant.getMatricule()))
                    && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                    && (mod.getCode().equals(module.getCode()))
                    && (note.getIsFinal().equals(false))){
                noteList.add(note);
            }
        }
//        double result = Math.round(valeur*100.0)/100.0;
        return new ResponseEntity<>(noteList, HttpStatus.OK);
    }

    //    Modifier une Note
    @PutMapping("/updateNote/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable("id") Long id, @RequestBody Note note){

        Note noteFromDb = noteRepo.findById(id).orElse(null);
        if (noteFromDb == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        noteFromDb.setSessions(note.getSessions());
        noteFromDb.setValeur(note.getValeur());


        return new ResponseEntity<>(noteRepo.save(noteFromDb), HttpStatus.OK);
    }

    //    Supprimer une Note
    @DeleteMapping("/deleteNote/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable("id") Long id){
        noteRepo.deleteById(id);
        return new ResponseEntity<>("Deleted with Successfully from database", HttpStatus.OK);
    }
}