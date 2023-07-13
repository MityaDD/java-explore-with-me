package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class StatDTO {
    Long id;
    @NotEmpty(message = "Идентификатор сервиса не может быть пустым")
    private String app;
    @NotEmpty(message = "URI не может быть пустым")
    private String uri;
    @NotEmpty(message = "IP-адрес пользователя не может быть пустым")
    private String ip;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
