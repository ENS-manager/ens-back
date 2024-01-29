package com.api.gestnotesapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PVCoursRequest {

    int session;
    private String code;
    private String anneeAca;
    private String matricule;
    private String parcours;
}
