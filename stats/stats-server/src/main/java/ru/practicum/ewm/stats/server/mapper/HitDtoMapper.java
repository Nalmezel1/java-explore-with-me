package ru.practicum.ewm.stats.server.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.HitDtoRequest;
import ru.practicum.ewm.stats.server.model.Hit;


@Service
public class HitDtoMapper {

    public Hit fromDto(HitDtoRequest dto) {
        return Hit.builder().app(dto.getApp()).uri(dto.getUri()).ip(dto.getIp()).timestamp(dto.getTimestamp()).build();
    }

    public HitDtoRequest toDto(Hit hit) {
        return HitDtoRequest.builder().app(hit.getApp()).uri(hit.getUri()).ip(hit.getIp()).build();
    }

}