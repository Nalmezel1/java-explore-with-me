package ru.practicum.ewm.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventDtoShort;
import ru.practicum.ewm.event.dto.EventDtoNew;
import ru.practicum.ewm.event.model.Event;

import java.util.List;


@Mapper(componentModel = "spring")
public interface EventMapper {
    EventDto toEventFullDto(Event event);

    @Mapping(source = "category", target = "category.id")
    Event toEventModel(EventDtoNew eventDtoNew);

    Event toEventModel(EventDto eventDto);

    List<EventDtoShort> toEventShortDtoList(List<Event> events);

    List<Event> toEventList(List<EventDto> events);

    List<EventDto> toEventFullDtoList(List<Event> events);
}