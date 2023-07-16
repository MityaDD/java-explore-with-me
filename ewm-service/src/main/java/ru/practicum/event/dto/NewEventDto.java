package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.model.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotNull
    @Size(min = 20, max = 2000)
    String annotation;
    @NotNull
    Long category;
    @NotNull
    @Size(min = 20, max = 7000)
    String description;
    @NotNull
    Location location;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    String eventDate;
    Boolean paid = Boolean.FALSE;
    Integer participantLimit = 0;
    Boolean requestModeration = Boolean.TRUE;
    @NotNull
    @Size(min = 3, max = 120)
    String title;
}
