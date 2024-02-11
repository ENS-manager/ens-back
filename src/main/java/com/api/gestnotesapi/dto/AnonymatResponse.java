package com.api.gestnotesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnonymatResponse {
    private String anneeAca;
    private String cours;
    private String parcours;
    private int session;
    List<AnonymatDto> anonymatDtoList;
}
