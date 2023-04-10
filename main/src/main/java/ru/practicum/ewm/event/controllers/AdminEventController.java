package ru.practicum.ewm.event.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventDtoAdminUpdate;
import ru.practicum.ewm.enums.EventState;
import ru.practicum.ewm.event.service.EventService;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class AdminEventController {
    private final EventService eventService;

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto updateEvent(@PathVariable(name = "eventId") Long eventId,
                                @Valid @RequestBody EventDtoAdminUpdate eventDtoAdminUpdate) {
        return eventService.updateEvent(eventId, eventDtoAdminUpdate);

    }

    @GetMapping
    public List<EventDto> getEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                    @RequestParam(name = "states", required = false) EventState states,
                                    @RequestParam(name = "categories", required = false) List<Long> categoriesId,
                                    @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                    @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                    @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                    @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return eventService.getEventsWithParamsByAdmin(users, states, categoriesId, rangeStart, rangeEnd, from, size);
    }
}
