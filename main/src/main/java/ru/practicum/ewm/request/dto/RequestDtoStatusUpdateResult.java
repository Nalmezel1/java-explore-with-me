package ru.practicum.ewm.request.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestDtoStatusUpdateResult {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}
