package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.dto.RequestDtoStatusUpdate;
import ru.practicum.ewm.request.dto.RequestDtoStatusUpdateResult;

import java.util.List;

public interface RequestService {

    RequestDto createRequest(Long userId, Long eventId);

    List<RequestDto> getRequestsByOwnerOfEvent(Long userId, Long eventId);

    RequestDtoStatusUpdateResult updateRequests(Long userId, Long eventId, RequestDtoStatusUpdate requestDtoStatusUpdate);

    List<RequestDto> getCurrentUserRequests(Long userId);

    RequestDto cancelRequests(Long userId, Long requestId);
}
