package ru.practicum.ewm.stats.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;


import org.springframework.stereotype.Controller;
import ru.practicum.ewm.stats.dto.HitDtoRequest;
import ru.practicum.ewm.stats.dto.HitDtoResponse;
import ru.practicum.ewm.stats.server.service.StatsServerService;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class StatsServerController {

    private final StatsServerService statsServerService;
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/hit")
    public ResponseEntity<HitDtoRequest> create(@Valid @RequestBody HitDtoRequest dtoRequest) {
        return new ResponseEntity<>(statsServerService.post(dtoRequest), HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<HitDtoResponse>> get(@RequestParam @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime start,
                                                    @RequestParam @DateTimeFormat(pattern = TIME_PATTERN) LocalDateTime end,
                                                    @RequestParam(required = false) Optional<List<String>> uris,
                                                    @RequestParam(defaultValue = "false") boolean unique) {
        return uris.map(strings -> new ResponseEntity<>(
                        statsServerService.getStats(start, end, strings, unique),
                        HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(
                        statsServerService.getStats(start, end, unique), HttpStatus.OK));

    }

}