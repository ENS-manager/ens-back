package com.api.gestnotesapi.dto;

import com.api.gestnotesapi.entities.Etudiant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PVGrandJuryResponse {
    String anneeAca;
    Integer cycle;
    String session;
    String promotion;
    List<PVGrandJuryDto> pvGrandJuryDtoList;
}
