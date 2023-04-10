package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationDtoNew;
import ru.practicum.ewm.compilation.dto.RequestDtoUpdateCompilation;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exceptions.CompilationNotExistException;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.EventService;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final EntityManager entityManager;
    private final CompilationRepository compilationRepository;
    private final CompilationMapper mapper;

    @Override
    @Transactional
    public CompilationDto createCompilation(CompilationDtoNew compilationDtoNew) {
        List<Event> events = eventRepository.findAllByIdIn(compilationDtoNew.getEvents());
        Compilation compilation = new Compilation();
        compilation.setEvents(new HashSet<>(events));
        compilation.setPinned(compilationDtoNew.getPinned());
        compilation.setTitle(compilationDtoNew.getTitle());

        Compilation savedCompilation = compilationRepository.save(compilation);
        return mapper.mapToCompilationDto(savedCompilation);
    }

    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotExistException("Подборка не существует"));
        return mapper.mapToCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Compilation> query = builder.createQuery(Compilation.class);

        Root<Compilation> root = query.from(Compilation.class);
        Predicate criteria = builder.conjunction();

        if (pinned != null) {
            Predicate isPinned;
            if (pinned) {
                isPinned = builder.isTrue(root.get("pinned"));
            } else {
                isPinned = builder.isFalse(root.get("pinned"));
            }
            criteria = builder.and(criteria, isPinned);
        }

        query.select(root).where(criteria);
        List<Compilation> compilations = entityManager.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();

        return mapper.mapToListCompilationDto(compilations);
    }

    @Transactional
    public CompilationDto updateCompilation(Long compId, RequestDtoUpdateCompilation requestDtoUpdateCompilation) {

        Compilation oldCompilation = compilationRepository.findById(compId).orElseThrow(() -> new CompilationNotExistException("Подборка не существует"));
        List<Long> eventsIds = requestDtoUpdateCompilation.getEvents();
        if (eventsIds != null) {
            List<Event> events = eventRepository.findAllByIdIn(requestDtoUpdateCompilation.getEvents());
            oldCompilation.setEvents(new HashSet<>(events));
        }
        if (requestDtoUpdateCompilation.getPinned() != null) {
            oldCompilation.setPinned(requestDtoUpdateCompilation.getPinned());
        }
        if (requestDtoUpdateCompilation.getTitle() != null) {
            oldCompilation.setTitle(requestDtoUpdateCompilation.getTitle());
        }
        Compilation updatedCompilation = compilationRepository.save(oldCompilation);
        return mapper.mapToCompilationDto(updatedCompilation);
    }

    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() -> new CompilationNotExistException("Подборка не существует"));
        compilationRepository.deleteById(compId);
    }

    private void setView(Compilation compilation) {
        Set<Event> setEvents = compilation.getEvents();
        if (!setEvents.isEmpty()) {
            List<Event> events = new ArrayList<>(setEvents);
            eventService.setView(events);
        }
    }
}