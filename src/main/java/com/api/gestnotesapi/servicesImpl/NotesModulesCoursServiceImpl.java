package com.api.gestnotesapi.servicesImpl;

import com.api.gestnotesapi.entities.AnneeAcademique;
import com.api.gestnotesapi.entities.Cours;
import com.api.gestnotesapi.entities.Module;
import com.api.gestnotesapi.entities.Note;
import com.api.gestnotesapi.repository.AnneeAcademiqueRepo;
import com.api.gestnotesapi.repository.CoursRepo;
import com.api.gestnotesapi.repository.ModuleRepo;
import com.api.gestnotesapi.repository.NoteRepo;
import com.api.gestnotesapi.services.NotesModulesCoursService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NotesModulesCoursServiceImpl implements NotesModulesCoursService {

    private CoursRepo coursRepo;
    private NoteRepo noteRepo;
    private ModuleRepo moduleRepo;
    private AnneeAcademiqueRepo anneeAcademiqueRepo;

    @Override
    public List<Note> getNotesModulesCours(int session, int year, String code) {
        List<Note> noteList = new ArrayList<>();
        Optional<Cours> cours = coursRepo.findByCode(code);
        AnneeAcademique anneeAcademique = anneeAcademiqueRepo.findByNumeroDebut(year);
        if (!cours.isPresent()){
            return null;
        }
        for (Note note : noteRepo.findAll()){
            Cours cour = coursRepo.findByCoursId(note.getCours().getCoursId());
            Module module = moduleRepo.findById(note.getModule().getId()).get();
            Cours coursModule = coursRepo.findByCoursId(module.getCours().getCoursId());
            AnneeAcademique anneeAca = anneeAcademiqueRepo.findById(note.getAnneeAcademique().getId()).get();
            if ((cour.getCode().equals(cours.get().getCode()))
                    && (coursModule.getCode().equals(cours.get().getCode()))
                    && (note.getIsFinal().equals(false))
                    && (anneeAca.getNumeroDebut().equals(anneeAcademique.getNumeroDebut()))
                    && (note.getSessions().equals(session))){

                noteList.add(note);
            }
        }
        return noteList;
    }
}
