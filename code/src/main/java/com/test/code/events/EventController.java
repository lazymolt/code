package com.test.code.events;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.net.URI;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.test.code.common.ErrorsResource;

@Controller
@RequestMapping(value="/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE) // 모든 핸들러들이 이 형식으로 응답함
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

//    @Autowired 생략가능
    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors){
        if(errors.hasErrors()) {
//            return ResponseEntity.badRequest().build();
            return badRequest(errors);
            // 이건 에러나는데 왜냐?
            // 자바빈 스펙을 따르기 떄문에 이벤트라는 도메인은, 빈 시리얼라이저를 사용해서 시리얼라이제이션 해줄수 있는데
            // errors는 자바빈 스펙을 준수하는 객체가 아니라 걍 시리얼라이제이션 안됨.
            // produces = MediaTypes.HAL_JSON_UTF8_VALUE 요 형태로 바꿔줄려는 시도
        }
        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()){
            return badRequest(errors);
        }
        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        Event newEvent = this.eventRepository.save(event);
        ControllerLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = selfLinkBuilder.toUri();
        EventResource eventResource = new EventResource(event);
//        eventResource.add(new Link());
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
//        eventResource.add(selfLinkBuilder.withSelfRel());
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        eventResource.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));
        return ResponseEntity.created(createdUri).body(eventResource);
    }

//    @GetMapping
//    public ResponseEntity queryEvents(Pageable pageable) {
//        return ResponseEntity.ok(this.eventRepository.findAll(pageable));
//    }

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }

}
