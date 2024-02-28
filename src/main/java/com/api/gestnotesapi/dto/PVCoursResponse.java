package com.api.gestnotesapi.dto;

import com.api.gestnotesapi.entities.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PVCoursResponse {

    int session;
    int credit;
    private String label;
    private String code;
    private String intitule;
    private String anneeAcademique;

    private List<PVCoursResponseDto> pvCoursResponseDtoList;

}
