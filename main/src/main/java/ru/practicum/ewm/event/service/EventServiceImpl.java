package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.HitDtoResponse;
import ru.practicum.ewm.category.service.CategoryServiceImpl;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventDtoShort;
import ru.practicum.ewm.event.dto.EventDtoNew;
import ru.practicum.ewm.event.dto.EventDtoAdminUpdate;
import ru.practicum.ewm.event.dto.EventDtoUserUpdate;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.enums.EventState;
import ru.practicum.ewm.enums.SortValue;
import ru.practicum.ewm.enums.StateActionForAdmin;
import ru.practicum.ewm.enums.StateActionForUser;
import ru.practicum.ewm.exceptions.AlreadyPublishedException;
import ru.practicum.ewm.exceptions.EventAlreadyCanceledException;
import ru.practicum.ewm.exceptions.EventNotExistException;
import ru.practicum.ewm.exceptions.WrongTimeException;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.statisticService.StatisticsService;
import ru.practicum.ewm.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.ewm.util.DateFormatConstant.TIME_PATTERN;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryServiceImpl categoryService;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;

    private final UserServiceImpl userService;

    private final EntityManager entityManager;
    private final String datePattern = TIME_PATTERN;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
    private final StatisticsService statisticsService;

    @Override
    public EventDto createEvent(Long userId, EventDtoNew newEventDto) {

        categoryService.checkCategoryId(newEventDto.getCategory());
        Category category = categoryRepository.getReferenceById(newEventDto.getCategory());

        LocalDateTime eventDate = newEventDto.getEventDate();
        dateCheck(eventDate);
        Event event = eventMapper.toEventModel(newEventDto);
        event.setCategory(category);

        userService.checkUserId(userId);
        User user = userRepository.getReferenceById(userId);

        event.setInitiator(user);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventDtoShort> getEvents(Long userId, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        return eventMapper.toEventShortDtoList(eventRepository.findAllByInitiatorId(userId, page).toList());
    }

    @Override
    public EventDto getEventByUser(Long userId, Long eventId) {
        return eventMapper.toEventFullDto(eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EventNotExistException("")));
    }

    @Override
    public List<EventDto> getEventsWithParamsByAdmin(List<Long> users, EventState states, List<Long> categoriesId,
                                                     String rangeStart, String rangeEnd, Integer from, Integer size) {
        LocalDateTime start = rangeStart != null ? LocalDateTime.parse(rangeStart, dateFormatter) : null;
        LocalDateTime end = rangeEnd != null ? LocalDateTime.parse(rangeEnd, dateFormatter) : null;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);

        Root<Event> root = query.from(Event.class);
        Predicate criteria = builder.conjunction();

        if (categoriesId != null && categoriesId.size() > 0) {
            Predicate containCategories = root.get("category").in(categoriesId);
            criteria = builder.and(criteria, containCategories);
        }

        if (users != null && users.size() > 0) {
            Predicate containUsers = root.get("initiator").in(users);
            criteria = builder.and(criteria, containUsers);
        }

        if (states != null) {
            Predicate containStates = root.get("state").in(states);
            criteria = builder.and(criteria, containStates);
        }

        if (rangeStart != null) {
            Predicate greaterTime = builder.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), start);
            criteria = builder.and(criteria, greaterTime);
        }
        if (rangeEnd != null) {
            Predicate lessTime = builder.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), end);
            criteria = builder.and(criteria, lessTime);
        }

        query.select(root).where(criteria);
        List<Event> events = entityManager.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();

        if (events.size() == 0) {
            return new ArrayList<>();
        }


        //setView(events);


        return eventMapper.toEventFullDtoList(events);
    }

    @Override
    public List<EventDto> getEventsWithParamsByUser(String text, List<Long> categories, Boolean paid, String rangeStart,
                                                    String rangeEnd, Boolean onlyAvailable, SortValue sort,
                                                    Integer from, Integer size, HttpServletRequest request) {
        LocalDateTime start = rangeStart != null ? LocalDateTime.parse(rangeStart, dateFormatter) : null;
        LocalDateTime end = rangeEnd != null ? LocalDateTime.parse(rangeEnd, dateFormatter) : null;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);

        Root<Event> root = query.from(Event.class);
        Predicate criteria = builder.conjunction();

        if (text != null) {
            Predicate annotationContain = builder.like(builder.lower(root.get("annotation")),
                    "%" + text.toLowerCase() + "%");
            Predicate descriptionContain = builder.like(builder.lower(root.get("description")),
                    "%" + text.toLowerCase() + "%");
            Predicate containText = builder.or(annotationContain, descriptionContain);

            criteria = builder.and(criteria, containText);
        }

        if (categories != null && categories.size() > 0) {
            Predicate containStates = root.get("category").in(categories);
            criteria = builder.and(criteria, containStates);
        }

        if (paid != null) {
            Predicate isPaid;
            if (paid) {
                isPaid = builder.isTrue(root.get("paid"));
            } else {
                isPaid = builder.isFalse(root.get("paid"));
            }
            criteria = builder.and(criteria, isPaid);
        }

        if (rangeStart != null) {
            Predicate greaterTime = builder.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), start);
            criteria = builder.and(criteria, greaterTime);
        }
        if (rangeEnd != null) {
            Predicate lessTime = builder.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), end);
            criteria = builder.and(criteria, lessTime);
        }

        query.select(root).where(criteria).orderBy(builder.asc(root.get("eventDate")));
        List<Event> events = entityManager.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();

        if (onlyAvailable) {
            events = events.stream()
                    .filter((event -> event.getConfirmedRequests() < (long) event.getParticipantLimit()))
                    .collect(Collectors.toList());
        }

        if (sort != null) {
            if (sort.equals(SortValue.EVENT_DATE)) {
                events = events.stream().sorted(Comparator.comparing(Event::getEventDate)).collect(Collectors.toList());
            } else {
                events = events.stream().sorted(Comparator.comparing(Event::getViews)).collect(Collectors.toList());
            }
        }

        if (events.size() == 0) {
            return new ArrayList<>();
        }

        statisticsService.sendStat(events, request);
        return eventMapper.toEventFullDtoList(events);
    }

    @Override
    public EventDto getEvent(Long id, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndPublishedOnIsNotNull(id)
                .orElseThrow(() -> new EventNotExistException("Ивент не существует"));
        statisticsService.sendStat(event, request);
        return eventMapper.toEventFullDto(event);
    }


    @Override
    public EventDto updateEvent(Long eventId, EventDtoAdminUpdate updateEventAdminDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotExistException("Ивент не найден"));
        if (updateEventAdminDto == null) {
            return eventMapper.toEventFullDto(event);
        }

        if (updateEventAdminDto.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminDto.getAnnotation());
        }
        if (updateEventAdminDto.getCategory() != null) {
            categoryService.checkCategoryId(updateEventAdminDto.getCategory());
            Category category = categoryRepository.getReferenceById(updateEventAdminDto.getCategory());

            event.setCategory(category);
        }
        if (updateEventAdminDto.getDescription() != null) {
            event.setDescription(updateEventAdminDto.getDescription());
        }
        if (updateEventAdminDto.getLocation() != null) {
            event.setLocation(updateEventAdminDto.getLocation());
        }
        if (updateEventAdminDto.getPaid() != null) {
            event.setPaid(updateEventAdminDto.getPaid());
        }
        if (updateEventAdminDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminDto.getParticipantLimit().intValue());
        }
        if (updateEventAdminDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminDto.getRequestModeration());
        }
        if (updateEventAdminDto.getTitle() != null) {
            event.setTitle(updateEventAdminDto.getTitle());
        }
        if (updateEventAdminDto.getStateAction() != null) {
            if (updateEventAdminDto.getStateAction().equals(StateActionForAdmin.PUBLISH_EVENT)) {
                if (event.getPublishedOn() != null) {
                    throw new AlreadyPublishedException("Иввент уже опубликован");
                }
                if (event.getState().equals(EventState.CANCELED)) {
                    throw new EventAlreadyCanceledException("Ивент уже удалён");
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (updateEventAdminDto.getStateAction().equals(StateActionForAdmin.REJECT_EVENT)) {
                if (event.getPublishedOn() != null) {
                    throw new AlreadyPublishedException("Ивент уже опубликован");
                }
                event.setState(EventState.CANCELED);
            }
        }
        if (updateEventAdminDto.getEventDate() != null) {
            LocalDateTime eventDateTime = updateEventAdminDto.getEventDate();
            if (eventDateTime.isBefore(LocalDateTime.now())
                    || eventDateTime.isBefore(event.getPublishedOn().plusHours(1))) {
                throw new WrongTimeException("Начало должно быть минимум за час до публикации");
            }

            event.setEventDate(updateEventAdminDto.getEventDate());
        }

        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventDto updateEventByUser(Long userId, Long eventId, EventDtoUserUpdate updateEventUserDto) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EventNotExistException(""));

        if (event.getPublishedOn() != null) {
            throw new AlreadyPublishedException("Ивент уже опубликован");
        }

        if (updateEventUserDto == null) {
            return eventMapper.toEventFullDto(event);
        }

        if (updateEventUserDto.getAnnotation() != null) {
            event.setAnnotation(updateEventUserDto.getAnnotation());
        }
        if (updateEventUserDto.getCategory() != null) {
            categoryService.checkCategoryId(updateEventUserDto.getCategory());
            Category category = categoryRepository.getReferenceById(updateEventUserDto.getCategory());
            event.setCategory(category);
        }
        if (updateEventUserDto.getDescription() != null) {
            event.setDescription(updateEventUserDto.getDescription());
        }
        if (updateEventUserDto.getEventDate() != null) {
            LocalDateTime eventDateTime = updateEventUserDto.getEventDate();
            if (eventDateTime.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new WrongTimeException("Начало должно быть мимнимум за час до публикации");
            }
            event.setEventDate(updateEventUserDto.getEventDate());
        }
        if (updateEventUserDto.getLocation() != null) {
            event.setLocation(updateEventUserDto.getLocation());
        }
        if (updateEventUserDto.getPaid() != null) {
            event.setPaid(updateEventUserDto.getPaid());
        }
        if (updateEventUserDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserDto.getParticipantLimit().intValue());
        }
        if (updateEventUserDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserDto.getRequestModeration());
        }
        if (updateEventUserDto.getTitle() != null) {
            event.setTitle(updateEventUserDto.getTitle());
        }

        if (updateEventUserDto.getStateAction() != null) {
            if (updateEventUserDto.getStateAction().equals(StateActionForUser.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else {
                event.setState(EventState.CANCELED);
            }
        }

        return eventMapper.toEventFullDto(eventRepository.save(event));
    }


    @Override
    public void setView(List<Event> events) {
        LocalDateTime start = events.get(0).getCreatedOn();
        List<String> uris = new ArrayList<>();
        Map<String, Event> eventsUri = new HashMap<>();
        String uri = "";
        for (Event event : events) {
            if (start.isBefore(event.getCreatedOn())) {
                start = event.getCreatedOn();
            }
            uri = "/events/" + event.getId();
            uris.add(uri);
            eventsUri.put(uri, event);
            event.setViews(0L);
        }

        LocalDateTime startTime = start;
        LocalDateTime endTime = LocalDateTime.now();

        List<HitDtoResponse> stats = statisticsService.getStats(startTime, endTime, uris);
        stats.forEach((stat) ->
                eventsUri.get(stat.getUri()).setViews(stat.getHits()));
    }

    public void dateCheck(LocalDateTime date) {
        if (date.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new WrongTimeException("Дата уже прошла");
        }
    }
}