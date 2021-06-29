package cn.hayring.detecttool.web;

import cn.hayring.detecttool.domain.Event;
import cn.hayring.detecttool.domain.Person;
import cn.hayring.detecttool.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
public class EventController {

    private EventService eventService;


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateEvent(@PathVariable Long id, Event event) {
        if (!id.equals(event.getId())) return ResponseEntity.badRequest().build();
        eventService.updateEvent(event);
        return ResponseEntity.noContent().build();
    }



    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }
}
