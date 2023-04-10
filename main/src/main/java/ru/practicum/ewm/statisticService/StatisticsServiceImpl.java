package ru.practicum.ewm.statisticService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exceptions.StatsException;
import ru.practicum.ewm.stats.dto.*;
import ru.practicum.ewm.stats.client.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatsClient statsClient;

    @Override
    public void sendStat(Event event, HttpServletRequest request) {
        LocalDateTime now = LocalDateTime.now();
        String remoteAddr = request.getRemoteAddr();
        String nameService = "main-service";

        HitDtoRequest requestDto = new HitDtoRequest();
        requestDto.setTimestamp(now);
        requestDto.setUri("/events");
        requestDto.setApp(nameService);
        requestDto.setIp(remoteAddr);
        statsClient.createHit(requestDto);
        sendStatForTheEvent(event.getId(), remoteAddr, now, nameService);
    }

    @Override
    public void sendStat(List<Event> events, HttpServletRequest request) {
        LocalDateTime now = LocalDateTime.now();
        String remoteAddr = request.getRemoteAddr();
        String nameService = "main-service";

        HitDtoRequest requestDto = new HitDtoRequest();
        requestDto.setTimestamp(now);
        requestDto.setUri("/events");
        requestDto.setApp(nameService);
        requestDto.setIp(request.getRemoteAddr());
        statsClient.createHit(requestDto);
        sendStatForEveryEvent(events, remoteAddr, LocalDateTime.now(), nameService);
    }

    @Override
    public void sendStatForTheEvent(Long eventId, String remoteAddr, LocalDateTime now,
                                    String nameService) {
        HitDtoRequest requestDto = new HitDtoRequest();
        requestDto.setTimestamp(now);
        requestDto.setUri("/events/" + eventId);
        requestDto.setApp(nameService);
        requestDto.setIp(remoteAddr);
        statsClient.createHit(requestDto);
    }

    @Override
    public void sendStatForEveryEvent(List<Event> events, String remoteAddr, LocalDateTime now,
                                      String nameService) {
        for (Event event : events) {
            HitDtoRequest requestDto = new HitDtoRequest();
            requestDto.setTimestamp(now);
            requestDto.setUri("/events/" + event.getId());
            requestDto.setApp(nameService);
            requestDto.setIp(remoteAddr);
            statsClient.createHit(requestDto);
        }
    }

    @Override
    public void setView(Event event) {
        LocalDateTime startTime = event.getCreatedOn();
        LocalDateTime endTime = LocalDateTime.now();
        List<String> uris = List.of("/events/" + event.getId());

        List<HitDtoResponse> stats = getStats(startTime, endTime, uris);
        if (stats.size() == 1) {
            event.setViews(stats.get(0).getHits());
        } else {
            event.setViews(0L);
        }
    }

    @Override
    public List<HitDtoResponse> getStats(LocalDateTime startTime, LocalDateTime endTime, List<String> uris) {

        ResponseEntity<String> response = statsClient.getStats(startTime, endTime, uris, false);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return Arrays.asList(objectMapper.readValue(response.getBody(), HitDtoResponse[].class));
        } catch (JsonProcessingException exception) {
            throw new StatsException(String.format("Json processing error: %s", exception.getMessage()));
        }


    }
}