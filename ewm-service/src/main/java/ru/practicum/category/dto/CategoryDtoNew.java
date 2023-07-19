package ru.practicum.category.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDtoNew {
    Long id;
    @NotBlank
    @Size(min = 1, max = 50)
    String name;
}
