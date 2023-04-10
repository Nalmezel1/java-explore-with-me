package ru.practicum.ewm.request.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

import static ru.practicum.ewm.util.DateFormatConstant.TIME_PATTERN;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestDto {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_PATTERN)
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private String status;
}
