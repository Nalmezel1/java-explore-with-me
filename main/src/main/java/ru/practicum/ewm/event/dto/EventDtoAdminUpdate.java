package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import ru.practicum.ewm.enums.StateActionForAdmin;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventDtoAdminUpdate extends EventDtoUpdate {
    private StateActionForAdmin stateAction;
}

