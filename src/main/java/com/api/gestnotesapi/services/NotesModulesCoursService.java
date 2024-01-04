package com.api.gestnotesapi.services;

import com.api.gestnotesapi.entities.Note;
import org.jvnet.hk2.annotations.Service;

import java.util.List;

public interface NotesModulesCoursService {

    public List<Note> getNotesModulesCours(int session, int year, String code);
}
