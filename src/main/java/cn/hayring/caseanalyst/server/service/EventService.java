package cn.hayring.caseanalyst.server.service;

import cn.hayring.caseanalyst.server.dao.EventDao;
import cn.hayring.caseanalyst.server.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private EventDao eventDao;



    @Value("${cn.hayring.caseanalyst.server.pagesize}")
    private int pageSize;

    public Page<Event> findAllEvent(Long id, int pageNo) {
        return eventDao.findAllByCaseIdInPage(id, PageRequest.of(pageNo, pageSize));
    }

    public int updateEvent(Event event) {
        eventDao.save(event);
        return 0;
    }


    public Event createNewEvent(Long caseId) {
        Long id = eventDao.createEvent(caseId);
        Event event = new Event();
        event.setId(id);
        return event;
    }

    @Autowired
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

}
