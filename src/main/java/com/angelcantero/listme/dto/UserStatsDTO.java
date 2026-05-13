package com.angelcantero.listme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStatsDTO {
    private long totalLibraries;
    private long totalItems;
}
