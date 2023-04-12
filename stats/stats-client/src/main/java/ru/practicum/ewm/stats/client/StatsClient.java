package ru.practicum.ewm.stats.client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.ewm.stats.dto.HitDtoRequest;


@Service
public class StatsClient extends BaseClient {

    private final RestTemplate restTemplate;

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new).build()
        );
        this.restTemplate = builder.build();
    }

    public ResponseEntity<Object> createHit(HitDtoRequest hitDtoRequest) {
        return post("/hit", hitDtoRequest);
    }

    public ResponseEntity<String> getStats(LocalDateTime start, LocalDateTime end,
                                           List<String> uri,
                                           boolean unique) {


        System.out.println("startTime - " + start);
        System.out.println("endTime - " + end);
        System.out.println("uris - " + uri);

        String path;
        Map<String, Object> parameters;

        if (!uri.equals(null)) {
            path = "/stats?start={start}&end={end}&uri={uri}&unique={unique}";
            parameters = Map.of(
                    "start", start.format(formatter),
                    "end", end.format(formatter),
                    "uri", Optional.of(uri).get(),
                    "unique", unique
            );
        } else {
            path = "/stats?start={start}&end={end}&unique={unique}";
            parameters = Map.of("start", start.format(formatter), "end", end.format(formatter),
                    "unique", unique
            );
        }

        ResponseEntity<String> response = restTemplate.getForEntity(path, String.class, parameters);


        return response;
    }
}