package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import ru.practicum.ewm.enums.StateActionForUser;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventDtoUserUpdate extends EventDtoUpdate {
    private StateActionForUser stateAction;
}

