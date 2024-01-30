package com.api.gestnotesapi.dto;

import com.api.gestnotesapi.entities.TYPE;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PVCoursRequest {

    int session;
    private String code;
    private TYPE type;
    private String anneeAca;
    private String parcours;
}
