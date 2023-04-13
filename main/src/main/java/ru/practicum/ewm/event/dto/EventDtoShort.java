package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.user.dto.UserDtoShort;

import java.time.LocalDateTime;

import static ru.practicum.ewm.util.DateFormatConstant.TIME_PATTERN;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventDtoShort {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_PATTERN)
    private LocalDateTime eventDate;
    private UserDtoShort initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
