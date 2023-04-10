package ru.practicum.ewm.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.enums.RequestStatusToUpdate;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestDtoStatusUpdate {
    private List<Long> requestIds;
    private RequestStatusToUpdate status;
}
