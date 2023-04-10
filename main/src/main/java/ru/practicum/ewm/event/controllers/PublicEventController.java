package ru.practicum.ewm.event.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.enums.SortValue;
import ru.practicum.ewm.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventDto> getEventsWithParamsByUser(@RequestParam(name = "text", required = false) String text,
                                                                                          @RequestParam(name = "categories", required = false) List<Long> categories,
                                                                                          @RequestParam(name = "paid", required = false) Boolean paid,
                                                                                          @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                                                                          @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                                                                          @RequestParam(name = "onlyAvailable", required = false) boolean onlyAvailable,
                                                                                          @RequestParam(name = "sort", required = false) SortValue sort,
                                                                                          @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                                                                          @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                                                                          HttpServletRequest request) {
        return eventService.getEventsWithParamsByUser(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{id}")
    public EventDto getEvent(@PathVariable Long id, HttpServletRequest request) {
        return eventService.getEvent(id, request);
    }
}