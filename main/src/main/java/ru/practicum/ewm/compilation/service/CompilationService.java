package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationDtoNew;
import ru.practicum.ewm.compilation.dto.RequestDtoUpdateCompilation;


import java.util.List;

public interface CompilationService {

    CompilationDto createCompilation(CompilationDtoNew compilationDtoNew);

    CompilationDto getCompilation(Long compId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, RequestDtoUpdateCompilation requestDtoUpdateCompilation);
}