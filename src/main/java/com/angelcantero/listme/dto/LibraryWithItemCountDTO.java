package com.angelcantero.listme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryWithItemCountDTO {
    private Long idLibrary;
    private String name;
    private Long itemCount;
    private int position;
}
