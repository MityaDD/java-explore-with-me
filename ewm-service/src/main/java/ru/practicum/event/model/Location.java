package ru.practicum.event.model;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {
    @Column(name = "lat")
    Double lat;
    @Column(name = "lon")
    Double lon;
}
