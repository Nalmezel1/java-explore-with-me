package ru.practicum.ewm.compilation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class RequestDtoUpdateCompilation {
    private List<Long> events;
    private boolean pinned;
    @Size(max = 550)
    private String title;
}
