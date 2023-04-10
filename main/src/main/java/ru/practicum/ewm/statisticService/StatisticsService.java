package ru.practicum.ewm.statisticService;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.stats.dto.HitDtoResponse;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {
    void sendStat(Event event, HttpServletRequest request);

    void sendStat(List<Event> events, HttpServletRequest request);

    void sendStatForTheEvent(Long eventId, String remoteAddr, LocalDateTime now,
                             String nameService);

    void sendStatForEveryEvent(List<Event> events, String remoteAddr, LocalDateTime now,
                               String nameService);

    void setView(Event event);

    List<HitDtoResponse> getStats(LocalDateTime startTime, LocalDateTime endTime, List<String> uris);
}
