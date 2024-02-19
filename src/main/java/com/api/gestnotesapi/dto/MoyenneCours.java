package com.api.gestnotesapi.dto;

import com.api.gestnotesapi.entities.Cours;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoyenneCours {
    String code;
    Double moy;
}
