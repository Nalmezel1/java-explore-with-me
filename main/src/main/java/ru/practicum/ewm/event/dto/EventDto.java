package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.location.LocationDto;
import ru.practicum.ewm.user.dto.UserDtoShort;
import ru.practicum.ewm.enums.EventState;

import java.time.LocalDateTime;

import static ru.practicum.ewm.util.DateFormatConstant.TIME_PATTERN;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventDto {
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_PATTERN)
    private LocalDateTime createdOn;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_PATTERN)
    private LocalDateTime eventDate;
    private Long id;
    private UserDtoShort initiator;
    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_PATTERN)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Long views;
}