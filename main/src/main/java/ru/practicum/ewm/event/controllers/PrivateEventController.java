package ru.practicum.ewm.event.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventDtoShort;
import ru.practicum.ewm.event.dto.EventDtoNew;
import ru.practicum.ewm.event.dto.EventDtoUserUpdate;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.dto.RequestDtoStatusUpdate;
import ru.practicum.ewm.request.dto.RequestDtoStatusUpdateResult;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class PrivateEventController {
    private final EventService eventService;

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@PathVariable Long userId, @Valid @RequestBody EventDtoNew eventDtoNew) {
        return eventService.createEvent(userId, eventDtoNew);
    }

    @GetMapping
    public List<EventDtoShort> getEventsByUser(@PathVariable Long userId,
                                               @RequestParam(name = "from", defaultValue = "0", required = false) Integer from,
                                               @RequestParam(name = "size", defaultValue = "10", required = false) Integer size) {
        return eventService.getEvents(userId, from, size);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestsByOwnerOfEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return requestService.getRequestsByOwnerOfEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public RequestDtoStatusUpdateResult updateRequests(@PathVariable Long userId, @PathVariable Long eventId, @RequestBody RequestDtoStatusUpdate requestDtoStatusUpdate) {
        return requestService.updateRequests(userId, eventId, requestDtoStatusUpdate);
    }

    @PatchMapping("/{eventId}")
    public EventDto updateEventByUser(@PathVariable Long userId,
                                      @PathVariable Long eventId,
                                      @Valid @RequestBody EventDtoUserUpdate eventDtoUserUpdate) {
        return eventService.updateEventByUser(userId, eventId, eventDtoUserUpdate);
    }

    @GetMapping("/{eventId}")
    public EventDto getEventByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getEventByUser(userId, eventId);
    }
}
