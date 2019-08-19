package com.test.code.events;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class EventResource extends Resource<Event> {

    public EventResource(Event event, Link... links) {
        super(event, links);
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
        // add(new Link("http://localhost:8080/api/events/"+event.getId())); // 이 방법과 같지만 타입세이프 하지 않다. 고쳐주기 힘듬.
    }

}

//public class EventResource extends ResourceSupport {
//
//    @JsonUnwrapped
//    private Event event;
//
//    public EventResource(Event event){
//        this.event = event;
//    }
//
//    public Event getEvent(){
//        return event;
//    }
//}
