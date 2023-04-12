package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventDtoShort;
import ru.practicum.ewm.event.dto.EventDtoNew;
import ru.practicum.ewm.event.dto.EventDtoAdminUpdate;
import ru.practicum.ewm.event.dto.EventDtoUserUpdate;
import ru.practicum.ewm.enums.EventState;
import ru.practicum.ewm.enums.SortValue;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventDto createEvent(Long userId, EventDtoNew eventDtoNew);

    List<EventDtoShort> getEvents(Long userId, Integer from, Integer size);

    EventDto updateEvent(Long eventId, EventDtoAdminUpdate eventDtoAdminUpdate);

    EventDto updateEventByUser(Long userId, Long eventId, EventDtoUserUpdate eventDtoUserUpdate);

    EventDto getEventByUser(Long userId, Long eventId);

    List<EventDto> getEventsWithParamsByAdmin(List<Long> users, EventState states, List<Long> categoriesId,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    List<EventDto> getEventsWithParamsByUser(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd, Boolean onlyAvailable, SortValue sort, Integer from,
                                             Integer size, HttpServletRequest request);

    EventDto getEvent(Long id, HttpServletRequest request);

}