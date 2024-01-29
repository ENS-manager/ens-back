package com.api.gestnotesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PVModuleRequest {

    private String code;
    private String anneeAca;
    private String matricule;
    private String parcours;
}
