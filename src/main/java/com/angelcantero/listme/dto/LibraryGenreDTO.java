package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryGenreDTO {
    private Long id;

    @NotNull(message = "El ID de la biblioteca es obligatorio")
    private Long libraryId;

    @NotBlank(message = "El nombre del género es obligatorio")
    private String name;
}
