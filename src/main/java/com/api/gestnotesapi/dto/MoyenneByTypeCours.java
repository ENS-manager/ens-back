package com.api.gestnotesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoyenneByTypeCours {
    Double moyFonda;
    Double moyProf;
    Double moyComp;
}
