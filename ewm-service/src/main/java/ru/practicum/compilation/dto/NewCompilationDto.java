package ru.practicum.compilation.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ru.practicum.compilation.dto.Validated.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCompilationDto {
    List<Long> events;
    Boolean pinned = Boolean.FALSE;
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    @Size(min = 2, max = 50, groups = {Create.class, Update.class})
    String title;
}
