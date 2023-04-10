package ru.practicum.ewm.compilation.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;

import java.util.List;


@Component
@Mapper(componentModel = "spring")
public interface CompilationMapper {
    CompilationDto mapToCompilationDto(Compilation compilation);
    List<CompilationDto> mapToListCompilationDto(List<Compilation> compilations);
}