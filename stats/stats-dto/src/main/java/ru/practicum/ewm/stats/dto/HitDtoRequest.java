package ru.practicum.ewm.stats.dto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Data
@Valid
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HitDtoRequest {
    @NotBlank
    private String app;
    @NotBlank
    private String uri;
    @NotBlank
    private String ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

}