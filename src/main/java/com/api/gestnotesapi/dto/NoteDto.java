package com.api.gestnotesapi.dto;

import com.api.gestnotesapi.entities.CodeEva;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDto {

    private Double valeur;
    private CodeEva code;
}
