package com.api.gestnotesapi.dto;

import com.api.gestnotesapi.entities.TYPE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PVGrandJuryRequest {

    String anneeAca;
    String parcours;
}
